package com.finleap.weatherforecast.controller;

import com.finleap.weatherforecast.constants.TimeConstants;
import com.finleap.weatherforecast.controller.data.TemperatureSummary;
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
class WeatherControllerIT {

    @Autowired
    private WeatherController weatherController;

    private static final String CITY = "London";
    private static final String UNKNOWN_CITY = "unknowncity";

    @Test
    void shouldReturnTemperatureSummary() {
        TemperatureSummary temperatureSummary = weatherController.getWeatherData(CITY);

        assertEquals(CITY, temperatureSummary.getCityName());
        assertEquals(TimeConstants.DEFAULT_TIMEZONE, temperatureSummary.getTimeZone());
        assertTrue(temperatureSummary.getDays().size() > 0);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUnknownCityName() {
        assertThrows(ResourceNotFoundException.class, () -> weatherController.getWeatherData(UNKNOWN_CITY));
    }
}
