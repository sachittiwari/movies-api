package com.sachit.moviesapi.repository;

import com.sachit.moviesapi.entity.Movies;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoviesRepository extends CrudRepository<Movies, Long> {

    @Query("Select m from Movies m ORDER BY m.rating DESC,CAST(REPLACE(REPLACE(SUBSTRING(m.boxOfficeValue, 2), ',', ''),'/A','0') AS int) DESC LIMIT 10")
    public List<Movies> findTop10ByRatingOrderByBoxOfficeValueDesc();

    public Optional<Movies> findByMovieTitleIgnoreCase(String movieTitle);
}
