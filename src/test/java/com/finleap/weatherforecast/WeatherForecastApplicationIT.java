package com.finleap.weatherforecast;

import com.finleap.weatherforecast.api.data.Misc;
import com.finleap.weatherforecast.constants.TimeConstants;
import com.finleap.weatherforecast.controller.data.TemperatureSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(JUnitPlatform.class)
@SpringBootTest(classes = WeatherForecastApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class WeatherForecastApplicationIT {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    private static final String ENDPOINT = "/data/";
    private static final String CITY = "London";
    private static final String UNKNOWN_CITY = "unknowncity";
    private static final String WRONG_CITY = "wrong12city";
    private static String TOO_LONG_CITY = "";

    @BeforeEach
    void setUp() {
        StringBuilder tooLongCityName = new StringBuilder();

        for (int i = 0; i < 51; i++) {
            tooLongCityName.append("a");
        }

        TOO_LONG_CITY = tooLongCityName.toString();
    }

    @Test
    void shouldContextLoads() {
        WeatherForecastApplication.main(new String[] {});
    }

    @Test
    void shouldReturnTemperatureSummary() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(ENDPOINT + CITY),
                HttpMethod.GET, entity, String.class);

        TemperatureSummary temperatureSummary = Misc.deserializeSafeJson(response.getBody(), TemperatureSummary.class);

        assertEquals(CITY, temperatureSummary.getCityName());
        assertEquals(TimeConstants.DEFAULT_TIMEZONE, temperatureSummary.getTimeZone());
        assertTrue(temperatureSummary.getDays().size() > 0);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUnknownCityName() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(ENDPOINT + UNKNOWN_CITY),
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldThrowBadRequestExceptionWhenWrongCityName() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(ENDPOINT + WRONG_CITY),
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldThrowBadRequestExceptionWhenTooLongCityName() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(ENDPOINT + TOO_LONG_CITY),
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
