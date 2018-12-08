package com.finleap.weatherforecast.api;

import com.finleap.weatherforecast.api.data.Misc;
import com.finleap.weatherforecast.api.data.WeatherData;
import com.finleap.weatherforecast.data.TemperatureData;
import com.finleap.weatherforecast.exceptions.InternalServiceException;
import com.finleap.weatherforecast.exceptions.ResourceNotFoundException;
import com.finleap.weatherforecast.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Open Weather API test")
class OpenWeatherMapAPITest {

    private static final String TEST_GATEWAY_URL = "gateway.url";
    private static final String TEST_API_KEY = "api.key";
    private static final String TEST_CITY_NAME = "London";
    private static final String TEST_WEATHER_DATA_SUCCESSFUL_RESPONSE_FILE = "weather_data_successful_response.json";
    private static final String TEST_TEMPERATURE_DATA_SUCCESSFUL_RESPONSE_FILE = "temperature_data_successful_response.json";
    private OpenWeatherMapAPI openWeatherMapAPI;
    private RestOperations weatherServiceRestOperations;

    @BeforeEach
    void setUp() {
        weatherServiceRestOperations = mock(RestOperations.class);
        openWeatherMapAPI = new OpenWeatherMapAPI(TEST_GATEWAY_URL, TEST_API_KEY, weatherServiceRestOperations);
    }

    @Test
    void shouldReturnAccurateTemperatureData() {
        WeatherData weatherData = Misc.deserializeSafeJson(TestUtils.getFile(TEST_WEATHER_DATA_SUCCESSFUL_RESPONSE_FILE), WeatherData.class);
        TemperatureData temperatureDataExpected = Misc.deserializeSafeJson(TestUtils.getFile(TEST_TEMPERATURE_DATA_SUCCESSFUL_RESPONSE_FILE), TemperatureData.class);

        when(weatherServiceRestOperations.getForObject(anyString(), any(), anyString(), anyString())).thenReturn(weatherData);

        TemperatureData temperatureDataActual = openWeatherMapAPI.getTemperatureForecast(TEST_CITY_NAME);

        assertEquals(temperatureDataExpected.getName(), temperatureDataActual.getName());
        assertEquals(temperatureDataExpected.getZoneId(), temperatureDataActual.getZoneId());
        assertEquals(temperatureDataExpected.getTemperatures().toString(), temperatureDataActual.getTemperatures().toString());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenEmptyDataRetrieved() {
        when(weatherServiceRestOperations.getForObject(anyString(), any(), anyString(), anyString())).thenReturn("");

        assertThrows(Exception.class, () -> openWeatherMapAPI.getTemperatureForecast(TEST_CITY_NAME));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmptyDataRetrieved() {
        when(weatherServiceRestOperations.getForObject(anyString(), any(), anyString(), anyString())).thenReturn("");

        assertThrows(Exception.class, () -> openWeatherMapAPI.getTemperatureForecast(TEST_CITY_NAME));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenNoDataRetrieved() {
        when(weatherServiceRestOperations.getForObject(anyString(), any(), anyString(), anyString())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> openWeatherMapAPI.getTemperatureForecast(TEST_CITY_NAME));
    }

    @Test
    void shouldThrowInternalServiceExceptionWhenHttpStatusCodeExceptionRetrieved() {
        HttpStatusCodeException e = new HttpServerErrorException(HttpStatus.BAD_GATEWAY);
        when(weatherServiceRestOperations.getForObject(anyString(), any(), anyString(), anyString())).thenThrow(e);

        assertThrows(ResourceNotFoundException.class, () -> openWeatherMapAPI.getTemperatureForecast(TEST_CITY_NAME));
    }

    @Test
    void shouldThrowInternalServiceExceptionWhenExceptionRetrieved() {
        when(weatherServiceRestOperations.getForObject(anyString(), any(), anyString(), anyString())).thenThrow();

        assertThrows(InternalServiceException.class, () -> openWeatherMapAPI.getTemperatureForecast(TEST_CITY_NAME));
    }
}