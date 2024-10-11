package com.sachit.moviesapi.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customConfig(){
        //add security requirement for the API key
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("apiKey"))
                .components(new Components().addSecuritySchemes("apiKey",new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("movies-api-key")));
    }
}
