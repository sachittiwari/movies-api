# To Do List

This document contains the list of tasks that could be done as further enhancements for the Movies-Api project.

## Enhancements
- **Add More Endpoints**: Add more endpoints to fetch the list of all the movies data like Genre, Year, etc.
- **Implement Scheduler**: Implement a scheduler like Quartz and write a sync job to fetch the latest movies data from OMDB API so that each time we need not call the OMDB API.
- **Add More Security**: Add more security features like OAuth2, JWT, etc.
- **Centralized Exception Handling**: Implement overall Exception Handling using `ControllerAdvice` to handle exceptions globally.
- **Add More Tests**: Add more integration tests and unit tests to increase the code coverage.
- **Database Credentials Encryption**: Encrypt the database credentials in the application.properties file.
- **Move the Oscar win logic to DB**: Add another attribute to the ``movies`` table and store ``oscar_won`` as true or false. This way the application need not check this every time from cache, rather do it once when record is being inserted in DB.