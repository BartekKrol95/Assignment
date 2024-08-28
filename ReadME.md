Documentation for Frontend Developers
You can find the documentation for frontend developers in the files IdeaSwagger.yaml and projectSwagger.yaml. If the Swagger plugin is not available, simply paste the content of these files into the Swagger Editor.

Technologies Used
- Spring Boot Starter Validation: Provides mechanisms for validating input data, ensuring data integrity within the application.

- Spring Boot Starter Web: Includes necessary components for building a web application, including HTTP server configuration, support for REST APIs, and more.
- Lombok: A tool that enhances code readability by automatically generating methods such as getter, setter, toString, etc. This makes the code more concise and readable.
- H2 Database: A lightweight, in-memory database primarily used for testing. It allows for quick testing without the need to configure an external database.
- Spring Boot Starter Test: Contains a set of tools for testing the application, including support for unit and integration tests.
- Spring Boot DevTools: A developer tool that accelerates development by automatically reloading the application after changes are made.
- MapStruct: A framework for automatically mapping between different object types, reducing the amount of code needed for converting objects, such as DTOs to entities.

Potential Project Enhancements
If additional time for project development is available, I would consider implementing several key enhancements to improve the security, scalability, and maintainability of the application:

- OAuth2 and JWT-based Security: Implementing authorization and authentication mechanisms using OAuth2 and JWT would ensure secure access management for the API.

- Database Versioning with Liquibase: To manage schema changes and ensure consistency across environments, I would implement Liquibase.

- Containerization with Docker: Adding Docker files would allow for easy testing of the application in different environments and its integration with other services.

- Monitoring: Implementing application monitoring using Prometheus and Grafana would enable real-time tracking of performance and application status.