package com.sachit.moviesapi.service;

import com.sachit.moviesapi.entity.Movies;
import com.sachit.moviesapi.model.MoviesMapper;
import com.sachit.moviesapi.model.MoviesResponseDTO;
import com.sachit.moviesapi.model.OmdbResponse;
import com.sachit.moviesapi.repository.MoviesRepository;
import com.sachit.moviesapi.util.CsvReaderOscarWinners;
import com.sachit.moviesapi.util.OmdbApiHelper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MoviesService {

    @Autowired
    private MoviesRepository moviesRepository;

    @Autowired
    OmdbApiHelper omdbApiHelper;

    @Autowired
    CsvReaderOscarWinners csvReaderOscarWinners;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    @Value("${omdb.base.url}")
    private String omdbBaseUrl;

    @Value("${academy.awards.csv.name}")
    private String csvName;

    private HashSet<String> set;

    @PostConstruct
    public void init() {
        this.set = csvReaderOscarWinners.readOscarWinners(csvName);
    }

    @Autowired
    private MoviesMapper moviesMapper;



    /**
     * Retrieves list of top 10 Movies based on Box Office Value.
     *
     * @return List of top 10 Movies based on Box Office Value
     */
    public List<MoviesResponseDTO> getTop10MoviesBasedOnRatingDescBoxOfficeDesc() {
         return moviesRepository.findTop10ByRatingOrderByBoxOfficeValueDesc()
                .stream()
                .map(moviesMapper::toMoviesResponseDTO)
                .collect(Collectors.toList());
    }



    /**
     * Saves the movie data from OMDB API to the database.
     *
     * @param movieTitle The title to search for
     * @return The ID of the movie saved in the database
     */
    public Long saveOMDBData(String movieTitle){
        OmdbResponse response = omdbApiHelper.getOmdbData(movieTitle, omdbBaseUrl, omdbApiKey,restTemplate);
        Movies movie = new Movies();
        movie.setMovieTitle(response.getTitle());
        movie.setBoxOfficeValue(response.getBoxOffice());
        movie.setRating(response.getImdbRating());
        Long movieId = moviesRepository.save(movie).getMovieId();
        return movieId;
    }



    /**
     * Submits the rating for a movie.
     *
     * @param movieTitle The title to search for
     * @param ratingValue The rating to submit
     * @return The updated movie details
     */
    public MoviesResponseDTO submitRating(String movieTitle, Double ratingValue) throws Exception{
        Optional<Movies> existingMovie = moviesRepository.findByMovieTitleIgnoreCase(movieTitle);
        if (!existingMovie.isPresent()) {
            log.info("Movie not present in DB with title: {}", movieTitle);
            Long newMovieId = saveOMDBData(movieTitle);
            existingMovie = moviesRepository.findById(newMovieId);
        }
        existingMovie.get().setRating(ratingValue);
        return moviesMapper.toMoviesResponseDTO(moviesRepository.save(existingMovie.get()));
    }



    /**
     * Checks if the movie won an Oscar for Best Picture.
     *
     * @param movieTitle The title to search for
     *
     * @return True if the movie won an Oscar, False otherwise
     */
    public Boolean checkIfMovieWonOscar(String movieTitle) throws Exception{
        Boolean didMovieWinOscar = false;
        Optional<Movies> existingMovie = moviesRepository.findByMovieTitleIgnoreCase(movieTitle);
        if (!existingMovie.isPresent()) {
            log.info("Movie not present in DB with title: {}", movieTitle);
            saveOMDBData(movieTitle);
        }
        if(this.set.contains(movieTitle))
            didMovieWinOscar = true;
        return didMovieWinOscar;
    }

}

