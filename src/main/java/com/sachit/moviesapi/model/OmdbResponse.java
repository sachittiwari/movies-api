package com.sachit.moviesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OmdbResponse {

    @JsonProperty("Title")
    private String title;
    private Double imdbRating;
    @JsonProperty("BoxOffice")
    private String boxOffice;
}
