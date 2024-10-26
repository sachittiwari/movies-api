package com.sachit.moviesapi.controller;

import com.sachit.moviesapi.constants.MoviesApiTestConstants;
import com.sachit.moviesapi.model.MoviesRequestDTO;
import com.sachit.moviesapi.model.MoviesResponseDTO;
import com.sachit.moviesapi.security.config.MoviesApiKeyFilter;
import com.sachit.moviesapi.service.MoviesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoviesController.class)
public class MoviesControllerTest {

    @Autowired
    WebApplicationContext context;

    @MockBean
    MoviesService moviesService;

    @Autowired
    private MoviesApiKeyFilter moviesApiKeyFilter;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(moviesApiKeyFilter).build();
    }


    @Test
    public void testGetTop10MoviesBasedOnRatingOrderByBoxOffice() throws Exception {
        //Arrange
        MoviesResponseDTO movie1 = new MoviesResponseDTO();
        movie1.setMovieId("1");
        movie1.setMovieTitle("Movie1");
        movie1.setBoxOfficeValue("$100");
        movie1.setRating(4.5);

        MoviesResponseDTO movie2 = new MoviesResponseDTO();
        movie2.setMovieId("2");
        movie2.setMovieTitle("Movie2");
        movie2.setBoxOfficeValue("$200");
        movie2.setRating(4.0);

        //Mock the service
        when(moviesService.getTop10MoviesBasedOnRatingDescBoxOfficeDesc()).thenReturn(Arrays.asList(movie1, movie2));

        //Act and Assert
        mockMvc.perform(get("/movies/getTop10MoviesBasedOnBoxOffice")
                        .header(MoviesApiTestConstants.HEADER_NAME,MoviesApiTestConstants.API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].movieId").value("1"))
                .andExpect(jsonPath("$[0].movieTitle").value("Movie1"))
                .andExpect(jsonPath("$[0].boxOfficeValue").value("$100"))
                .andExpect(jsonPath("$[0].rating").value(4.5))
                .andExpect(jsonPath("$[1].movieId").value("2"))
                .andExpect(jsonPath("$[1].movieTitle").value("Movie2"))
                .andExpect(jsonPath("$[1].boxOfficeValue").value("$200"))
                .andExpect(jsonPath("$[1].rating").value(4.0));

        verify(moviesService, times(1)).getTop10MoviesBasedOnRatingDescBoxOfficeDesc();

    }

    @Test
    public void testGetTop10MoviesBasedOnRatingDescBoxOfficeDescWithoutApiKey() throws Exception {

        //Act and Assert
        mockMvc.perform(get("/movies/getTop10MoviesBasedOnBoxOffice"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testGetTop10MoviesBasedOnRatingDescBoxOfficeDescThrowsError() throws Exception {
        //Mock the service
        when(moviesService.getTop10MoviesBasedOnRatingDescBoxOfficeDesc()).thenThrow(new RuntimeException("Error occurred while fetching top 10 movies based on box office value"));


        //Act and Assert
        mockMvc.perform(get("/movies/getTop10MoviesBasedOnBoxOffice")
                .header(MoviesApiTestConstants.HEADER_NAME,MoviesApiTestConstants.API_KEY))
                .andExpect(status().is5xxServerError());

    }

    @Test
    public void testProvideRating() throws Exception {
        //Arrange
        MoviesRequestDTO movie = new MoviesRequestDTO();
        movie.setMovieTitle("Movie1");
        movie.setRating(4.5);

        MoviesResponseDTO movie1 = new MoviesResponseDTO();
        movie1.setMovieId("1");
        movie1.setMovieTitle("Movie1");
        movie1.setBoxOfficeValue("$100");
        movie1.setRating(4.5);

        //Mock the service
        when(moviesService.submitRating("Movie1", 4.5)).thenReturn(movie1);

        //Act and Assert
        mockMvc.perform(post("/movies/provideRating")
                        .header(MoviesApiTestConstants.HEADER_NAME,MoviesApiTestConstants.API_KEY)
                        .contentType("application/json")
                        .content("{\"movieTitle\":\"Movie1\",\"rating\":4.5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value("1"))
                .andExpect(jsonPath("$.movieTitle").value("Movie1"))
                .andExpect(jsonPath("$.boxOfficeValue").value("$100"))
                .andExpect(jsonPath("$.rating").value(4.5));

        verify(moviesService, times(1)).submitRating("Movie1", 4.5);
    }

    @Test
    public void testSubmitRatingThrowsError() throws Exception {
        //Mock the service
        when(moviesService.submitRating("Movie1",4.5)).thenThrow(new RuntimeException("Movie Not Found"));


        //Act and Assert
        mockMvc.perform(post("/movies/provideRating")
                        .header(MoviesApiTestConstants.HEADER_NAME,MoviesApiTestConstants.API_KEY))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void testSubmitRatingWithoutApiKey() throws Exception {

        //Act and Assert
        mockMvc.perform(post("/movies/provideRating"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testWonOscarWithTrueResponse() throws Exception {
        //Arrange
        String movieTitle = "Movie1";

        //Mock the service
        when(moviesService.checkIfMovieWonOscar(movieTitle)).thenReturn(true);

        //Act and Assert
        mockMvc.perform(post("/movies/checkOscarWon")
                        .header(MoviesApiTestConstants.HEADER_NAME,MoviesApiTestConstants.API_KEY)
                        .param("movieTitle", movieTitle))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(moviesService, times(1)).checkIfMovieWonOscar(movieTitle);


    }

    @Test
    public void testWonOscarWithFalseResponse() throws Exception {
        //Arrange
        String movieTitle = "Movie1";

        //Mock the service
        when(moviesService.checkIfMovieWonOscar(movieTitle)).thenReturn(false);

        //Act and Assert
        mockMvc.perform(post("/movies/checkOscarWon")
                        .header(MoviesApiTestConstants.HEADER_NAME,MoviesApiTestConstants.API_KEY)
                        .param("movieTitle", movieTitle))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(moviesService, times(1)).checkIfMovieWonOscar(movieTitle);


    }

    @Test
    public void testWonOscarThrowsError() throws Exception {
        //Mock the service
        when(moviesService.checkIfMovieWonOscar("Movie1")).thenThrow(new RuntimeException("Movie Not Found"));


        //Act and Assert
        mockMvc.perform(post("/movies/checkOscarWon")
                        .header(MoviesApiTestConstants.HEADER_NAME,MoviesApiTestConstants.API_KEY))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void testWonOscarWithoutApiKey() throws Exception {

        //Act and Assert
        mockMvc.perform(post("/movies/checkOscarWon"))
                .andExpect(status().isUnauthorized());

    }

}
