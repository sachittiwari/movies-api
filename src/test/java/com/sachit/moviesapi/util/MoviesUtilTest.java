package com.sachit.moviesapi.util;

import com.sachit.moviesapi.model.OmdbResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class MoviesUtilTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    OmdbApiHelper omdbApiHelper;

    @InjectMocks
    CsvReaderOscarWinners csvReaderOscarWinners;


    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReadOscarWinners_HappyPath() throws IOException {
        //Read the csv from classpath
        String csvFile = "oscar_winners.csv";

        // Call the method
        HashSet<String> result = csvReaderOscarWinners.readOscarWinners(csvFile);

        // Assert the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Movie1"));
        assertTrue(result.contains("Movie2"));
    }

    @Test
    public void testReadOscarWinners_NoBestPictureWinners() throws IOException {
        //Read csv from classpath
        String csvFile = "oscar_winners_no_best_picture.csv";

        // Call the method
        HashSet<String> result = csvReaderOscarWinners.readOscarWinners(csvFile);

        // Assert the result
        assertNotNull(result);
        assertTrue(result.isEmpty());  // No Best Picture winner
    }

    @Test
    public void testReadOscarWinners_InvalidFilePath() {
        // Call the method with an invalid file path
        HashSet<String> result = csvReaderOscarWinners.readOscarWinners("invalid/path/to/file.csv");

        // Assert that the result is an empty HashSet and no exception was thrown
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetOmdbData_Success(){
        //Arrange
        String movieTitle = "Inception";
        String omdbApiKey = "6cd4633";
        String omdbBaseUrl = "http://www.omdbapi.com";

        OmdbResponse omdbResponse = new OmdbResponse();
        omdbResponse.setTitle("Inception");
        omdbResponse.setBoxOffice("$1000000");
        omdbResponse.setImdbRating(8.8);

        //Mock
        when(restTemplate.getForObject(omdbBaseUrl + "?t=" + movieTitle + "&apikey=" + omdbApiKey, OmdbResponse.class))
                .thenReturn(omdbResponse);

        //Act
        OmdbResponse response = omdbApiHelper.getOmdbData(movieTitle, omdbBaseUrl, omdbApiKey, restTemplate);

        //Assert
        assertNotNull(response);
        assertEquals("Inception", response.getTitle());
        assertEquals(8.8, response.getImdbRating());

    }
}
