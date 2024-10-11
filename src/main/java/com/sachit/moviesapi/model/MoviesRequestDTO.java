package com.sachit.moviesapi.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class MoviesRequestDTO {

    private String movieTitle;

    @Min(value=0, message = "Rating should be at least 0")
    @Max(value=10, message = "Rating should be maximum 10")
    private Double rating;
}
