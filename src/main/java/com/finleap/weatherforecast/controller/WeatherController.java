package com.finleap.weatherforecast.controller;

import com.finleap.weatherforecast.constants.Messages;
import com.finleap.weatherforecast.controller.data.TemperatureSummary;
import com.finleap.weatherforecast.service.WeatherService;
import com.finleap.weatherforecast.validator.ValidCity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static javax.servlet.http.HttpServletResponse.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 *
 * Rest Controller to provide weather temperature data
 *
 * @author guneskahraman
 *
 */
@RestController
@Validated
@RequestMapping(value = {"/data"})
public class WeatherController {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(value = "/{city}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Get Weather Data")
    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "", response = TemperatureSummary.class),
            @ApiResponse(code = SC_BAD_REQUEST, message = Messages.BAD_REQUEST_MESSAGE, response = String.class),
            @ApiResponse(code = SC_UNAUTHORIZED, message = Messages.UNAUTHORIZED_MESSAGE, response = String.class),
            @ApiResponse(code = SC_FORBIDDEN, message = Messages.FORBIDDEN_MESSAGE, response = String.class),
            @ApiResponse(code = SC_NOT_FOUND, message = Messages.NOT_FOUND_MESSAGE, response = String.class)})
    public TemperatureSummary getWeatherData(@ApiParam(required = true) @ValidCity @PathVariable String city) {
        LOG.info("STEP-IN getWeatherData for city:{}", city);
        return weatherService.getWeather(city);
    }
}
