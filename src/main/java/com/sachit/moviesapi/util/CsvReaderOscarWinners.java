package com.sachit.moviesapi.util;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class CsvReaderOscarWinners {


    /**
     * Retrieves a set of oscar winning movies.
     *
     * @param csvFilePath path of the csv file with Oscar details
     * @return A set of movies which won Oscars for Best Picture
     */
    public HashSet<String> readOscarWinners(String csvFilePath) {
        HashSet<String> oscarWinners = new HashSet<>();
        //read data from classpath rather than absolute path
        ClassPathResource classPathResource = new ClassPathResource(csvFilePath);

            try (CSVReader reader = new CSVReader(new InputStreamReader(classPathResource.getInputStream()))) {
                List<String[]> records = reader.readAll();

                for (String[] record : records) {
                    String category = record[1].trim();
                    String nominee = record[2].trim();
                    String won = record[4].trim();

                    if (category.equalsIgnoreCase("Best Picture") && won.equalsIgnoreCase("YES")) {
                        oscarWinners.add(nominee);
                    }
                }


        } catch (Exception e) {
            log.error("Unable to capture data from csv file", e);
        }
        return oscarWinners;
    }
}
