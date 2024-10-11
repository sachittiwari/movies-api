package com.sachit.moviesapi.model;

import com.sachit.moviesapi.entity.Movies;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MoviesMapper {
    MoviesResponseDTO toMoviesResponseDTO(Movies movies);

}
