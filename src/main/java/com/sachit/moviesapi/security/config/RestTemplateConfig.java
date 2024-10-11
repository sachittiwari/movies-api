package com.sachit.moviesapi.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    //create bean for Rest Template to make API calls to OMDB API
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
