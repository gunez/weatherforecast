package com.finleap.weatherforecast.api;

import com.finleap.weatherforecast.api.data.Misc;
import com.finleap.weatherforecast.api.data.WeatherData;
import com.finleap.weatherforecast.constants.TimeConstants;
import com.finleap.weatherforecast.data.Temperature;
import com.finleap.weatherforecast.data.TemperatureData;
import com.finleap.weatherforecast.exceptions.InternalServiceException;
import com.finleap.weatherforecast.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 *
 * Open Weather Map API client.
 * Queries weather data and provides a filtered version of that data
 * to the services.
 *
 * @author guneskahraman
 *
 */
public class OpenWeatherMapAPI {
    private static final Logger LOG = LoggerFactory.getLogger(OpenWeatherMapAPI.class);

    private String gatewayUrl;
    private String apiKey;
    private RestOperations weatherServiceRestOperations;

    public OpenWeatherMapAPI(String gatewayUrl, String apiKey, RestOperations weatherServiceRestOperations) {
        this.gatewayUrl = gatewayUrl;
        this.apiKey = apiKey;
        this.weatherServiceRestOperations = weatherServiceRestOperations;
    }

    /**
     *
     * Retrieves weather data and returns extracted temperature data
     *
     * @param cityName as city name to query weather data from api
     * @return temperature data
     */
    public TemperatureData getTemperatureForecast(String cityName) {
        WeatherData weatherData = getWeatherForecast(cityName);

        if (LOG.isTraceEnabled()) {
            LOG.trace("Weather data: {}", Misc.serializeSafeJson(weatherData));
        }

        return extractTemperatureData(weatherData);
    }

    /**
     *
     * Queries weather data for a given city from open weather api
     *
     * @param cityName as city name to query weather data from api
     * @return weather data
     */
    private WeatherData getWeatherForecast(String cityName) {
        try {
            WeatherData weatherData = weatherServiceRestOperations.getForObject(gatewayUrl + "data/2.5/forecast?q={city}&units=metric&appid={apiKey}", WeatherData.class, cityName, apiKey);

            if (weatherData == null) {
                throw new ResourceNotFoundException("MappedTemperatureData " + cityName + " cannot be found");
            }

            LOG.info("Weather data retrieved for city:{}", cityName);

            return weatherData;
        } catch (final ResourceNotFoundException e) {
            LOG.error("No data retrieved from OpenWeatherMapService! city: {}", cityName, e);
            throw e;
        } catch (final HttpStatusCodeException e) {
            LOG.error("Failed to get weather data from OpenWeatherMapService! city: {}, Status code: {}", cityName, e.getStatusCode(), e);
            throw new ResourceNotFoundException(e.getMessage());
        } catch (final Exception e) {
            LOG.error("Unable to get weather data from OpenWeatherMapService! city: {}", cityName, e);
            throw new InternalServiceException("Failed to get weather data! Error message: " + e.getMessage(), e);
        }
    }

    /**
     *
     * Extracts temperature data from weather data, which is retrieved from open weather api
     *
     * @param weatherData as weather data retrieved from api
     * @return temperature data
     */
    private TemperatureData extractTemperatureData(WeatherData weatherData) {
        TemperatureData temperatureData = new TemperatureData(weatherData.getCity().getName(), Instant.now(), TimeConstants.DEFAULT_TIMEZONE);

        temperatureData.setTemperatures(weatherData.getList().stream().map(data -> new Temperature(data.getDt(), data.getMain().getTemp())).collect(Collectors.toList()));

        LOG.info("Temperature data extracted for city:{}", weatherData.getCity().getName());

        return temperatureData;
    }
}
