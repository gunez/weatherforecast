package com.finleap.weatherforecast.controller;

import com.finleap.weatherforecast.api.data.Misc;
import com.finleap.weatherforecast.controller.data.TemperatureSummary;
import com.finleap.weatherforecast.exceptions.InternalServiceException;
import com.finleap.weatherforecast.exceptions.ResourceNotFoundException;
import com.finleap.weatherforecast.service.WeatherService;
import com.finleap.weatherforecast.util.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WeatherController.class)
@DisplayName("Weather controller test")
class WeatherControllerTest {

    private static final String TEST_CITY_NAME = "London";
    private static final String URL = "/data/";
    private static final String REQUEST = URL + TEST_CITY_NAME;
    private static final String BAD_REQUEST = URL + "bad.request.1";
    private static final String TEST_TEMPERATURE_SUMMARY_SUCCESSFUL_RESPONSE_FILE = "temperature_summary_successful_response.json";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WeatherService weatherService;

    @Test
    void shouldReturnTemperatureSummary() throws Exception {
        // prepare data and mock's behaviour
        TemperatureSummary temperatureSummaryExpected = Misc.deserializeSafeJson(TestUtils.getFile(TEST_TEMPERATURE_SUMMARY_SUCCESSFUL_RESPONSE_FILE), TemperatureSummary.class);
        when(weatherService.getWeather(TEST_CITY_NAME)).thenReturn(temperatureSummaryExpected);

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REQUEST).accept(MediaType.APPLICATION_JSON)).andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status, "Incorrect Response Status");

        // verify that service method was called once
        verify(weatherService).getWeather(anyString());

        TemperatureSummary temperatureSummaryActual = Misc.deserializeSafeJson(result.getResponse().getContentAsString(), TemperatureSummary.class);

        assertNotNull(temperatureSummaryActual, "TemperatureSummary not found");
        assertEquals(temperatureSummaryExpected.toString(), temperatureSummaryActual.toString(), "Incorrect temperature summary");
    }

    @Test
    void shouldThrowNotFoundWhenResourceNotFound() throws Exception {
        // prepare data and mock's behaviour
        when(weatherService.getWeather(TEST_CITY_NAME)).thenThrow(ResourceNotFoundException.class);

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REQUEST).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals(HttpStatus.NOT_FOUND.value(), status, "Incorrect Response Status");

        // verify that service method was called once
        verify(weatherService).getWeather(anyString());
    }

    @Test
    void shouldThrowInternalServerErrorWhenInternalServiceException() throws Exception {
        // prepare data and mock's behaviour
        when(weatherService.getWeather(TEST_CITY_NAME)).thenThrow(InternalServiceException.class);

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REQUEST).accept(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError()).andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status, "Incorrect Response Status");

        // verify that service method was called once
        verify(weatherService).getWeather(anyString());
    }

    @Test
    void shouldThrowValidationExceptionWhenInternalServiceException() throws Exception {
        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(BAD_REQUEST).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals(HttpStatus.BAD_REQUEST.value(), status, "Incorrect Response Status");
    }
}