# Scale the Application

This document provides a step-by-step guide on how to scale the Movies-Api application for the service to behave efficiently and effectively under a wide range of workloads.

### Databases
- **Database Sharding**: Implement database sharding to distribute the data across multiple databases. This will ensure that single Database Server does not take the full load and go down during many concurrent requests. The data can be sharded based on multiple columns like ``Genre``, ``Year``, etc. If no uniform distribution is found, sharding using a hash function can also be done. In this case, it would be best to implement based on ``oscar_won`` column(Yes/No). 
- **Database Write-Read Replica**: Implement a database write-read replica to distribute the read and write operations across multiple databases. This will ensure that the read operations are not blocked by the write operations. Read operations can cover the API for ``fetchTop10MoviesBasedOnBoxOffice`` and also if the movies already exist in the database. Write Database will only be called to provide a rating and/or insert the new movie from OMDB API in our DB.
- **Database Indexing**: Implement indexing based on the ``Movie Title`` and ``Oscar Won``. This will help reduce load on DB.

### Caching:
- **Redis Cache**: Implement Redis Cache to cache the frequently accessed data. This will help reduce the load on the database and improve the performance of the application. The data that can be cached are the movies that have already been queried for ``Oscar Won`` or ``Rating Submission``. This will help in reducing the load on the database and improve the performance of the application.

### Load Balancer:
- **Any Load Balancer**: Implement any Load Balancer to distribute the incoming requests across multiple instances of the application. This will ensure that the load is distributed evenly across all the instances and the application is highly available.

### API Gateway:
- **API Gateway**: Implement an API Gateway like Kong to manage all the incoming requests and route them to the appropriate services. This will help in managing the incoming requests and also provide additional features like rate limiting and authentication. This will ensure that cases wherein many requests are being received, rate limiting will prevent that from bombarding the application and only permit certain requests at a time. Authentication can be handled in a centralized way.

### Monitoring:
- **Monitoring Tools**: Implement monitoring tools like Prometheus and Grafana to monitor the performance of the application. This will help in identifying the bottlenecks and optimizing the application for better performance.

### Auto-Scaling:
- **Auto-Scaling**: Implement Auto-Scaling to automatically scale the application based on the incoming traffic. This will ensure that the application can handle the load during peak times and scale down during low traffic. This can be done using Kubernetes or Cloud native Auto-scaling services.