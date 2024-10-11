# How to run the Application

This document provides a step-by-step guide on how to run the Movies-Api application.

## Prerequisites
Before running the application, please ensure the below components are installed and available

- **Java 17**: This application is developed using Java 17. Please ensure that it is installed either directly into the system or via the IDE.
- **Maven**: This application is built using Maven. Please ensure that it is installed and available in the system.
- **PostgreSQL**: This application uses PostgreSQL as the database. Please ensure that it is installed and running on the system.

## Steps to run the application
- **Import Source Code**: Extract the project folder from the zip file and import in your preferred IDE.
- **Setting Up Database**: 
    - Create a new database in PostgreSQL with the name `movies_api`.
    - Update the `application.properties` file with the database connection details.
- **Generate OMDB API Key**: 
    - Generate an API Key from the [OMDB API](http://www.omdbapi.com/apikey.aspx) website.
    - Update the `application.properties` file with the generated API Key. The name of the property is `omdb.api.key`
- **Place CSV File**: 
    - Place the `academy_awards.csv` file in any desired location and give the path in `application.properties` file for the property `csv.file.path`. It is also present in /src/main/resources folder.
- **Build the Application**: Build the application using the below command
    ```shell
    mvn clean install
- **Run the Application**: Run the application using the below command
    ```shell
    mvn spring-boot:run
-  You can also run the application by navigating to the `MoviesApiApplication` class and running it as a Java Application.
-  Another way to run the application is by navigating to the targets folder of the project and running the below command.
    ```shell
    java -jar movies-api-0.0.1-SNAPSHOT.jar
- **Access the Application**: The application can be accessed using the below URL
    ```shell
    http://localhost:8082/swagger-ui.html
