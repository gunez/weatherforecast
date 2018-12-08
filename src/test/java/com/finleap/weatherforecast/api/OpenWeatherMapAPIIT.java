package com.finleap.weatherforecast.api;

import com.finleap.weatherforecast.constants.TimeConstants;
import com.finleap.weatherforecast.data.TemperatureData;
import com.finleap.weatherforecast.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitPlatform.class)
@SpringBootTest
@ExtendWith(SpringExtension.class)
class OpenWeatherMapAPIIT {

    @Autowired
    private OpenWeatherMapAPI openWeatherMapAPI;

    private static final String CITY = "London";
    private static final String UNKNOWN_CITY = "unknowncity";

    @Test
    void shouldReturnTemperatureData() {
        TemperatureData temperatureData = openWeatherMapAPI.getTemperatureForecast(CITY);

        assertEquals(CITY, temperatureData.getName());
        assertEquals(TimeConstants.DEFAULT_TIMEZONE, temperatureData.getZoneId());
        assertTrue(temperatureData.getTemperatures().size() > 0);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUnknownCityName() {
        assertThrows(ResourceNotFoundException.class, () -> openWeatherMapAPI.getTemperatureForecast(UNKNOWN_CITY));
    }
}
