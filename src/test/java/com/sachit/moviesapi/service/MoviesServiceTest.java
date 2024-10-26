package com.sachit.moviesapi.service;

import com.sachit.moviesapi.entity.Movies;
import com.sachit.moviesapi.model.MoviesMapper;
import com.sachit.moviesapi.model.MoviesResponseDTO;
import com.sachit.moviesapi.model.OmdbResponse;
import com.sachit.moviesapi.repository.MoviesRepository;
import com.sachit.moviesapi.util.CsvReaderOscarWinners;
import com.sachit.moviesapi.util.OmdbApiHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MoviesServiceTest.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MoviesServiceTest {

    @Mock
    private MoviesRepository moviesRepository;

    @Mock
    private RestTemplate restTemplate;


    @Mock
    MoviesMapper moviesMapper;

    @Mock
    private OmdbApiHelper omdbApiHelper;

    @Mock
    private CsvReaderOscarWinners csvReaderOscarWinners;



    @Spy
    @InjectMocks
    private MoviesService moviesService;


    @BeforeEach
    public void setUp() throws Exception {

        MockitoAnnotations.openMocks(this);

        //set up test data using reflection
        HashSet<String> set = new HashSet<>();
        set.add("Movie1");

        //using reflection to set the private field
        Field setField = MoviesService.class.getDeclaredField("set");
        setField.setAccessible(true);
        setField.set(moviesService, set);

    }

    @Test
    public void testGetTop10MoviesBasedOnBoxOffice() {
        //Arrange
        Movies movies = new Movies();
        movies.setMovieId(1L);
        movies.setMovieTitle("Movie1");
        movies.setBoxOfficeValue(1000000.0);
        movies.setRating(4.5);

        //Mock repository
        when(moviesRepository.findTop10ByRatingOrderByBoxOfficeValueDesc()).thenReturn(Arrays.asList(movies));

        //Mock the mapper method
        MoviesResponseDTO moviesResponseDTO = new MoviesResponseDTO();
        moviesResponseDTO.setMovieId("1");
        moviesResponseDTO.setMovieTitle("Movie1");
        moviesResponseDTO.setBoxOfficeValue(1000000.0);
        moviesResponseDTO.setRating(4.5);
        when(moviesMapper.toMoviesResponseDTO(movies)).thenReturn(moviesResponseDTO);

        //Call the service method
        List<MoviesResponseDTO> moviesResponseDTOList = moviesService.getTop10MoviesBasedOnBoxOffice();

        //Assertions
        assertEquals(1, moviesResponseDTOList.size());
        assertEquals("Movie1", moviesResponseDTOList.get(0).getMovieTitle());
        assertEquals(1000000.0, moviesResponseDTOList.get(0).getBoxOfficeValue());
        assertEquals(4.5, moviesResponseDTOList.get(0).getRating());

        verify(moviesRepository,times(1)).findTop10ByRatingOrderByBoxOfficeValueDesc();

    }

    @Test
    public void testSubmitRatingInDbMovie() throws Exception {
        //Arrange
        Movies movies = new Movies();
        movies.setMovieTitle("Movie1");
        movies.setRating(4.5);

        MoviesResponseDTO moviesResponseDTO = new MoviesResponseDTO();
        moviesResponseDTO.setMovieTitle("Movie1");
        moviesResponseDTO.setRating(8.5);


        //Mock
        when(moviesRepository.findByMovieTitleIgnoreCase("Movie1")).thenReturn(Optional.of(movies));
        when(moviesMapper.toMoviesResponseDTO(movies)).thenReturn(moviesResponseDTO);
        when(moviesRepository.save(any())).thenReturn(movies);

        //Call the service method
        MoviesResponseDTO updatedMovieWithRating = moviesService.submitRating(moviesResponseDTO.getMovieTitle(), moviesResponseDTO.getRating());

        //Assertions
        assertEquals("Movie1", updatedMovieWithRating.getMovieTitle());
        assertEquals(8.5, updatedMovieWithRating.getRating());

        verify(moviesRepository,times(1)).findByMovieTitleIgnoreCase("Movie1");
        verify(moviesRepository,times(1)).save(movies);


    }

    @Test
    public void testSubmitRatingNotInDbMovie() throws Exception {
        //Arrange
        Movies movies = new Movies();
        movies.setMovieTitle("Movie1");
        movies.setRating(4.5);

        MoviesResponseDTO moviesResponseDTO = new MoviesResponseDTO();
        moviesResponseDTO.setMovieTitle("Movie1");
        moviesResponseDTO.setRating(8.5);


        //Mock
        when(moviesRepository.findByMovieTitleIgnoreCase("Movie1")).thenReturn(Optional.empty());
        doReturn(1l).when(moviesService).saveOMDBData("Movie1");
        when(moviesRepository.findById(1L)).thenReturn(Optional.of(movies));
        when(moviesMapper.toMoviesResponseDTO(movies)).thenReturn(moviesResponseDTO);
        when(moviesRepository.save(any())).thenReturn(movies);

        //Call the service method
        MoviesResponseDTO updatedMovieWithRating = moviesService.submitRating("Movie1", 8.5);

        //Assertions
        assertEquals("Movie1", updatedMovieWithRating.getMovieTitle());
        assertEquals(8.5, updatedMovieWithRating.getRating());

        verify(moviesRepository,times(1)).findByMovieTitleIgnoreCase("Movie1");
        verify(moviesRepository,times(1)).save(movies);


    }

    @Test
    public void testCheckOscarWonTrueInDb() throws Exception {
        //Arrange
        Movies movies = new Movies();
        movies.setMovieTitle("Movie1");
        movies.setRating(4.5);

        HashSet<String> set = new HashSet<>();
        set.add("Movie1");

        //Mock repository
        when(moviesRepository.findByMovieTitleIgnoreCase("Movie1")).thenReturn(Optional.of(movies));
        //mock the static method for CSV Reader
        when(csvReaderOscarWinners.readOscarWinners(anyString())).thenReturn(set);

        //Call the service method
        Boolean result = moviesService.checkIfMovieWonOscar("Movie1");

        //Assertions
        assertTrue(result);

        verify(moviesRepository,times(1)).findByMovieTitleIgnoreCase("Movie1");


    }

    @Test
    public void testCheckOscarWonFalseNotInDB() throws Exception {
        //Arrange
        Movies movies = new Movies();
        movies.setMovieTitle("Movie2");
        movies.setRating(4.5);

        HashSet<String> set = new HashSet<>();
        set.add("Movie1");


        //Mock repository
        when(moviesRepository.findByMovieTitleIgnoreCase("Movie1")).thenReturn(Optional.empty());
        doReturn(1l).when(moviesService).saveOMDBData("Movie2");
        //mock the static method for CSV Reader
        when(csvReaderOscarWinners.readOscarWinners(anyString())).thenReturn(set);

        //Call the service method
        Boolean result = moviesService.checkIfMovieWonOscar("Movie2");

        //Assertions
        assertFalse(result);

        verify(moviesRepository,times(1)).findByMovieTitleIgnoreCase("Movie2");


    }

    @Test
    public void testSaveOmdbData(){
        //Arrange
        Movies movies = new Movies();
        movies.setMovieId(100L);
        movies.setMovieTitle("Movie3");
        movies.setBoxOfficeValue(1000000.0);
        movies.setRating(4.5);

        OmdbResponse omdbResponse = new OmdbResponse();
        omdbResponse.setTitle("Movie3");
        omdbResponse.setBoxOffice("$1000000");
        omdbResponse.setImdbRating(4.5);

        //Mock
        when(moviesRepository.save(any())).thenReturn(movies);
        when(omdbApiHelper.getOmdbData("Movie3", null, null, restTemplate)).thenReturn(omdbResponse);


        //Call the service method
        Long movieId = moviesService.saveOMDBData("Movie3");

        //Assertions
        assertEquals(100L, movieId);
        verify(moviesRepository,times(1)).save(any());


    }



}
