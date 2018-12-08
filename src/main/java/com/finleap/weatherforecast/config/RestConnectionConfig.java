package com.finleap.weatherforecast.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConnectionConfig {
    @Bean
    public RestTemplate weatherServiceTemplate() {
        return new RestTemplate();
    }
}
