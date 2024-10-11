package com.sachit.moviesapi.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MoviesApiKeyFilter extends OncePerRequestFilter {

    @Value("${movies.api.key}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //fetch the incoming api key from the request
        String incomingApiKey = request.getHeader("movies-api-key");

        //check if the incoming api if for Swagger UI or API docs
        if(request.getRequestURI().startsWith("/swagger-ui") || request.getRequestURI().startsWith("/v3/api-docs")){
            filterChain.doFilter(request, response);
            return;
        }

        //check if the incoming api key is valid
        else if (incomingApiKey != null && incomingApiKey.equals(apiKey)) {
            SecurityContextHolder.getContext().setAuthentication(new MoviesApiKeyAuthentication(incomingApiKey, AuthorityUtils.NO_AUTHORITIES));
            filterChain.doFilter(request, response);
            return;
        }

        // return an unauthorized response since api key is invalid
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Invalid API Key\"}");
        response.getWriter().flush();
        response.getWriter().close();

    }

}
