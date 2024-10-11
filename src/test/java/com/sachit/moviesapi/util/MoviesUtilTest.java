package com.sachit.moviesapi.util;

import com.sachit.moviesapi.model.OmdbResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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


    private static Path tempCsvFile;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        // Create a temporary CSV file for testing
        tempCsvFile = Files.createTempFile("oscar_winners", ".csv");
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Delete the temporary file after each test
        Files.deleteIfExists(tempCsvFile);
    }

    @Test
    public void testReadOscarWinners_HappyPath() throws IOException {
        // Create a CSV content with Best Picture winners
        String csvContent = "Year,Category,Nominee,SomeColumn,Won\n" +
                "2020,Best Picture,Movie1,,YES\n" +
                "2021,Best Picture,Movie2,,YES\n" +
                "2021,Best Actor,Actor1,,NO\n";

        // Write the CSV content to the temporary file
        Files.write(tempCsvFile, csvContent.getBytes());

        // Call the method
        HashSet<String> result = csvReaderOscarWinners.readOscarWinners(tempCsvFile.toString());

        // Assert the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Movie1"));
        assertTrue(result.contains("Movie2"));
    }

    @Test
    public void testReadOscarWinners_NoBestPictureWinners() throws IOException {
        // Create a CSV content without any Best Picture winners
        String csvContent = "Year,Category,Nominee,SomeColumn,Won\n" +
                "2020,Best Actor,Actor1,,NO\n" +
                "2021,Best Director,Director1,,YES\n";

        // Write the CSV content to the temporary file
        Files.write(tempCsvFile, csvContent.getBytes());

        // Call the method
        HashSet<String> result = csvReaderOscarWinners.readOscarWinners(tempCsvFile.toString());

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
