package com.finleap.weatherforecast.service;

import com.finleap.weatherforecast.api.OpenWeatherMapAPI;
import com.finleap.weatherforecast.api.data.Misc;
import com.finleap.weatherforecast.constants.TimeConstants;
import com.finleap.weatherforecast.controller.data.Day;
import com.finleap.weatherforecast.controller.data.TemperatureSummary;
import com.finleap.weatherforecast.data.MappedTemperatureData;
import com.finleap.weatherforecast.data.Temperature;
import com.finleap.weatherforecast.data.TemperatureData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 *
 * Service retrieves raw weather temperature data and runs a logic
 * to calculate summary data of temperatures
 *
 * @author guneskahraman
 *
 */
@Service
public class WeatherService {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherService.class);

    private final OpenWeatherMapAPI openWeatherMapAPI;

    @Autowired
    public WeatherService(OpenWeatherMapAPI openWeatherMapAPI) {
        this.openWeatherMapAPI = openWeatherMapAPI;
    }

    /**
     *
     * Calls open weather map api to retrieve forecasts of weather data for a given city.
     * Calculates and returns temperature summary for a given city by using helper methods.
     * This method return results from cache if exists.
     * Cache is cleaned once in day at midnight (00:00)
     *
     * @param cityName as city name to query
     * @return temperature summary
     */
    @Cacheable("temperatureSummary")
    public TemperatureSummary getWeather(String cityName) {

        TemperatureData temperatureData = openWeatherMapAPI.getTemperatureForecast(cityName);

        MappedTemperatureData mappedTemperatureData = mapTemperatureData(temperatureData);

        TemperatureSummary temperatureSummary = prepareSummary(mappedTemperatureData);

        if (LOG.isTraceEnabled()) {
            LOG.trace("Temperature data: {}", Misc.serializeSafeJson(temperatureData));
            LOG.trace("Mapped temperature data: {}", Misc.serializeSafeJson(mappedTemperatureData));
            LOG.trace("Summary temperature data: {}", Misc.serializeSafeJson(temperatureSummary));
        }

        return temperatureSummary;
    }

    /**
     *
     * Maps temperature data regarding to their days
     *
     * @param temperatureData as raw data of temperatures
     * @return temperature data which is mapped to days
     */
    private MappedTemperatureData mapTemperatureData(TemperatureData temperatureData) {

        Map<String, List<Temperature>> dayTemperatures = temperatureData.getTemperatures().stream()
                .collect(Collectors.groupingBy(Temperature::getRegardingLocalDate));

        LOG.info("Temperature data mapped to days for city:{}", temperatureData.getName());

        return new MappedTemperatureData(temperatureData.getName(), TimeConstants.DEFAULT_TIMEZONE, temperatureData.getInstant(), dayTemperatures);
    }

    /**
     *
     * Calculates summary data for each day
     *
     * @param mappedTemperatureData as temperature data which is mapped to days
     * @return temperature summary
     */
    private TemperatureSummary prepareSummary(MappedTemperatureData mappedTemperatureData) {

        List<Day> days = new ArrayList<>();
        LocalDate localDate = LocalDateTime.ofInstant(mappedTemperatureData.getInstant(), ZoneId.of(TimeConstants.DEFAULT_TIMEZONE)).toLocalDate().plusDays(1);

        for (int i = 0; i < 3; i++, localDate = localDate.plusDays(1)) {
            double[] temperatures = this.splitTemperaturesByTime(localDate, mappedTemperatureData);

            int dailyAvgTemperature = Math.toIntExact(Math.round(temperatures[1] / temperatures[0]));
            int nightlyAvgTemperature = Math.toIntExact(Math.round(temperatures[3] / temperatures[2]));

            days.add(new Day(localDate, dailyAvgTemperature, nightlyAvgTemperature));
        }

        LOG.info("Summary data prepared for city:{}", mappedTemperatureData.getName());

        return new TemperatureSummary(mappedTemperatureData.getName(), TimeConstants.DEFAULT_TIMEZONE, days);
    }

    /**
     *
     * Splits temperature data as daily or nightly for a given day
     *
     * @param localDate as local date
     * @param mappedTemperatureData as temperature data which is mapped to days
     * @return double[4]
     */
    private double[] splitTemperaturesByTime(LocalDate localDate, MappedTemperatureData mappedTemperatureData) {
        double[] temperatures = {0, 0, 0, 0};

        mappedTemperatureData.getDayTemperatures().get(localDate.toString()).forEach(temperature -> {
            if (this.isDaily(temperature.getLocalTime())) {
                temperatures[0] += 1;
                temperatures[1] += temperature.getTemp();
            } else {
                temperatures[2] += 1;
                temperatures[3] += temperature.getTemp();
            }
        });

        LOG.debug("Mapped temperature data split as day/night for day: {} and city:{}", localDate, mappedTemperatureData.getName());

        return temperatures;
    }

    private boolean isDaily(LocalTime localTime) {
        return (localTime.equals(TimeConstants.DAY_START_TIME) || localTime.isAfter(TimeConstants.DAY_START_TIME)) && localTime.isBefore(TimeConstants.NIGHT_START_TIME);
    }
}
