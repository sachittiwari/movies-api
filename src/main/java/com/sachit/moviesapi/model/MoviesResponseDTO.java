package com.sachit.moviesapi.model;
import lombok.Data;

@Data
public class MoviesResponseDTO {

    private String movieId;
    private String movieTitle;
    private Double boxOfficeValue;
    private Double rating;

}
