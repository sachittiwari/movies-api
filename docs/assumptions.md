# Assumptions

This document contains the list of assumptions made while developing the Movies-Api project.

## Assumptions List
- **OMDB API Key**: The OMDB API Key is required to fetch the movie information. The API Key is generated from the [OMDB API](http://www.omdbapi.com/apikey.aspx) website. This is to be fetched manually and inserted into the application.
- **Data Storage**: To have an effective management of storing movies data in our database, only the movies that have been queried for check Oscar Won or Rating Submission will be stored in the database.
- **API Key**: The API Key required to access all the endpoints is hard-coded within the `application.properties`.
- **Swagger-UI**: The Swagger UI will not need any authentication to access the endpoints. The API Key is only required to access the endpoints.
- **CSV File**: The `academy_awards.csv` file will be present externally and its path is provided in the `application.properties` file.