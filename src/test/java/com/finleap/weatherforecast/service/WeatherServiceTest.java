package com.finleap.weatherforecast.service;

import com.finleap.weatherforecast.api.OpenWeatherMapAPI;
import com.finleap.weatherforecast.api.data.Misc;
import com.finleap.weatherforecast.controller.data.TemperatureSummary;
import com.finleap.weatherforecast.data.TemperatureData;
import com.finleap.weatherforecast.util.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Weather service test")
class WeatherServiceTest {

    private static final String TEST_CITY_NAME = "London";
    private static final String TEST_TEMPERATURE_DATA_SUCCESSFUL_RESPONSE_FILE = "temperature_data_successful_response.json";
    private static final String TEST_TEMPERATURE_SUMMARY_SUCCESSFUL_RESPONSE_FILE = "temperature_summary_successful_response.json";
    @InjectMocks
    private WeatherService weatherService;
    @Mock
    private OpenWeatherMapAPI openWeatherMapAPI;

    @Test
    void shouldReturnTemperatureSummary() {
        TemperatureData temperatureData = Misc.deserializeSafeJson(TestUtils.getFile(TEST_TEMPERATURE_DATA_SUCCESSFUL_RESPONSE_FILE), TemperatureData.class);
        TemperatureSummary temperatureSummaryExpected = Misc.deserializeSafeJson(TestUtils.getFile(TEST_TEMPERATURE_SUMMARY_SUCCESSFUL_RESPONSE_FILE), TemperatureSummary.class);
        when(openWeatherMapAPI.getTemperatureForecast(anyString())).thenReturn(temperatureData);

        TemperatureSummary temperatureSummaryActual = weatherService.getWeather(TEST_CITY_NAME);

        assertEquals(temperatureSummaryExpected.toString(), temperatureSummaryActual.toString());
    }
}