package com.sachit.moviesapi.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class MoviesWebSecurityConfig{

    @Autowired
    private MoviesApiKeyFilter moviesApiKeyFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        //authenticate all requests to /movies/**
                        .requestMatchers("/movies/**").authenticated()
                        //permit all requests to swagger ui and api docs
                        .requestMatchers("/swagger-ui/**","/swagger-ui.html","v3/api-docs/**").permitAll()
                        //deny all other requests
                        .anyRequest().denyAll())
                //add the custom filter before the UsernamePasswordAuthenticationFilter
                .addFilterBefore(moviesApiKeyFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }




}
