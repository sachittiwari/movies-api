package com.sachit.moviesapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "MOVIES")
public class Movies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ID")
    private Long movieId;

    @Column(name = "MOVIE_TITLE")
    private String movieTitle;

    @Column(name = "BOX_OFFICE_VALUE")
    private String boxOfficeValue;

    @Column(name = "RATING")
    private Double rating;


}
