# How to test the Application

This document provides a step-by-step guide on how to test the Movies-Api application and its endpoints.

## Steps to test the application
- **Access the Swagger UI**: Open the Swagger UI by navigating to the below URL in the browser
    ```shell
    http://localhost:8082/swagger-ui.html
    ```
- **Insert the API Key**: 
    - Click on the `Authorize` button in the top right corner of the Swagger UI.
    - Enter the value for movies-api-key in the input field and click on the `Authorize` button.
    - This value can be found in the `application.properties` file with the name `movies.api.key`.
    - All endpoints will now be accessible for testing.

- **Test the Oscar Winner Endpoint**:
    - Click on the `checkOscarWon` endpoint.
    - Enter the movie title in the `movieTitle` input field.
    - Click on the `Try it out` button.
    - The response will be displayed in the `Response Body` section.
    - If the movie does not exist, the response will be `404 Not Found` which indicates `Movie not found`.
- **Test the Movie Rating Endpoint**:
    - Click on the `provideRating` endpoint.
    - Enter the movie title in the `movieTitle` input field.
    - Enter the rating in the `rating` input field on a scale of 1-10.
    - Click on the `Try it out` button.
    - The response will be displayed in the `Response Body` section.
    - If the movie does not exist, the response will be `404 Not Found` which indicates `Movie not found`.
- **Test the Top 10 Movies Endpoint**:
    - Click on the `getTop10MoviesBasedOnBoxOffice` endpoint.
    - Click on the `Try it out` button.
    - The response will be displayed in the `Response Body` section.
    - The response will contain the list of top 10 movies by their Box Office Value.
- **Note**:
  - The API Key is required to access all the endpoints. If the API Key is not provided, the response will be `401 Unauthorized`.
  - Only the movies searched or rated will be persisted in the application database and visible in `getTop10MoviesBasedOnBoxOffice` endpoint.
