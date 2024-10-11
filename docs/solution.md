# Movies-Api Backbase Assignment

## Overview
This project is a Java based Web Application that provides RESTful APIs for users to check if any given movie has won the Oscar for "Best Picture" and also allows them to rate movies and fetch the list of top 10 movies by their Box Office Value.
It utilizes the OMDB API to fetch the information about the movies and a csv containing data about the Oscar awards from 1927 to 2010.

### Features
- **Oscar Winner Check**: Users can check if a movie has won the Oscar for "Best Picture".
- **Movie Rating**: Users can rate movies on a scale of 1 to 10.
- **Top 10 Movies**: Users can fetch the list of top 10 movies by their Box Office Value.

### Technologies Used
- **Java**: The core language used for development.
- **Spring Boot**: The framework used for building the application.
- **Spring Data JPA**: The library used for interacting with the database.
- **PostgreSQL**: The database used for storing the data.
- **OpenCSV**: The library used for reading the data from the csv file.
- **Swagger**: The library used for generating the API documentation and interface.
- **Lombok**: The library used for reducing boilerplate code.
- **JUnit 5**: The library used for writing the unit tests.
- **Liquibase**: The library used for managing the database migrations.
- **Spring Security**: The library used for securing the application with API KEY.
- **Spring Boot Validation**: The library used for validating the request parameters.

### Design Decisions:
- **Data Storage**: To have an effective management of storing movies data in our database, only the movies that have been queried for check Oscar Won or Rating Submission will be stored in the database.
