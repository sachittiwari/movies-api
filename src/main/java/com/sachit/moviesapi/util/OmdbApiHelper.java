package com.sachit.moviesapi.util;

import com.sachit.moviesapi.model.OmdbResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OmdbApiHelper {

    /**
     * Retrieves the response from OMDB API with Movie Details for the given movie Title.
     *
     * @param movieTitle The title to search for
     * @param omdbBaseUrl OMDB base URL to connect
     * @param omdbApiKey API Key to connect to OMDB API
     * @param restTemplate RestTemplate to make API calls
     * @return OmdbResponse object with movie details
     */
    public OmdbResponse getOmdbData(String movieTitle,String omdbBaseUrl,String omdbApiKey,RestTemplate restTemplate){
        StringBuilder url = new StringBuilder(omdbBaseUrl);
        url.append("?t=").append(movieTitle).append("&apikey=").append(omdbApiKey);
        OmdbResponse response = restTemplate.getForObject(url.toString(),OmdbResponse.class);
        return response;
    }

}
