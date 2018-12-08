package com.finleap.weatherforecast.config;

import com.finleap.weatherforecast.api.OpenWeatherMapAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenWeatherMapServiceClientConfig {
    private final RestTemplate weatherServiceTemplate;
    @Value("${openweather.service.url}")
    private String gatewayUrl;
    @Value("${openweather.service.apiKey}")
    private String apiKey;

    @Autowired
    public OpenWeatherMapServiceClientConfig(@Qualifier("weatherServiceTemplate") RestTemplate weatherServiceTemplate) {
        this.weatherServiceTemplate = weatherServiceTemplate;
    }

    @Bean
    public OpenWeatherMapAPI openWeatherMapAPI() {
        return new OpenWeatherMapAPI(gatewayUrl, apiKey, weatherServiceTemplate);
    }
}
