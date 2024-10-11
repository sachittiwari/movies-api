package com.sachit.moviesapi.repository;

import com.sachit.moviesapi.entity.Movies;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoviesRepository extends CrudRepository<Movies, Long> {

    public List<Movies> findTop10ByOrderByBoxOfficeValueDesc();

    public Optional<Movies> findByMovieTitle(String movieTitle);
}
