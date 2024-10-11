package com.sachit.moviesapi.controller;

import com.sachit.moviesapi.model.MoviesRequestDTO;
import com.sachit.moviesapi.model.MoviesResponseDTO;
import com.sachit.moviesapi.service.MoviesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Slf4j
@Validated
public class MoviesController {

    @Autowired
    MoviesService moviesService;



    @Operation(summary = "Get top 10 movies based on box office value",
               description = "This API returns top 10 movies based on box office value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of top 10 Movies",
                    content=@Content(mediaType = "application/json",schema = @Schema(implementation = MoviesResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Unable to retrieve list of top 10 Movies",
                    content=@Content(mediaType = "application/json",schema = @Schema()))
    })
    @GetMapping("/getTop10MoviesBasedOnBoxOffice")
    public ResponseEntity<List<MoviesResponseDTO>> getTop10MoviesBasedOnBoxOffice() {
        try {
            List<MoviesResponseDTO> movies = moviesService.getTop10MoviesBasedOnBoxOffice();
            return ResponseEntity.ok(movies);
        }
        catch(Exception e){
            log.error("Error occurred while fetching top 10 movies based on box office value", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "Submit rating for a movie",
            description = "This API will update the ratings provided for a specific movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully stored the rating for the movie",
                    content=@Content(mediaType = "application/json",schema = @Schema(implementation = MoviesResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No Movie Available with the given name",
                    content=@Content(mediaType = "application/json",schema = @Schema()))
    })
    @PostMapping("/provideRating")
    public ResponseEntity<MoviesResponseDTO> provideRating(@Valid @RequestBody MoviesRequestDTO movie) {
        try {
            MoviesResponseDTO newRating = moviesService.submitRating(movie.getMovieTitle(), movie.getRating());
            return ResponseEntity.ok(newRating);
        }
        catch(Exception e){
            log.error("No Movie Available with the given name", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Fetches information about movie winning Oscar",
            description = "This API will check the movie details based on the title and update if it has won Oscar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Oscar Result",
                    content=@Content(mediaType = "text/plain",schema = @Schema(implementation = MoviesResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No movie with the given name",
                    content=@Content(mediaType = "text/plain",schema = @Schema()))
    })
    @PostMapping("/checkOscarWon")
    public ResponseEntity<String> wonOscar(@Parameter(description = "Provide the Title of the Movie") @RequestParam String movieTitle) {
        try {
            Boolean wonOscar = moviesService.checkIfMovieWonOscar(movieTitle);
            return ResponseEntity.ok(wonOscar.toString());
        }
        catch(Exception e){
            log.error("No Movie Available with the given name", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
