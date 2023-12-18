# Car Rest Microservice

This is a demonstrative Java Spring Boot project which is built in a microservice style. The main idea is to provide secured and robust CRUD operations access to the car database through the REST interface.

## Table of Contents
- [Project Description](#project-description)
- [Technology Stack](#technology-stack)
- [Resource Service](#resource-service)
- [Authentication Service](#authentication-service)
- [Technology Stack](#technology-stack)
- [How to Install and Run the Project](#how-to-install-and-run-the-project)
- [Credits](#credits)

## Project Description
The Car Rest Microservice is designed to provide a secure and flexible API for any size Car database. It allows users to easily make any CRUD operation in case they are authenticated, otherwise users are limited only to retrieve data.
Also, Microservice architecture allows easily separate main duties and guarantees the reliability of the whole system.

## Technology Stack

- **Backend**:
  - [Java](https://www.java.com/) - A general-purpose programming language that is class-based, object-oriented, and designed to have as few implementation dependencies as possible.
  - [Spring Framework](https://spring.io/) - An application framework and inversion of control container for the Java platform.
    - [Spring Boot](https://spring.io/projects/spring-boot) - An extension of the Spring framework that simplifies the process of building production-ready applications.
    - [Spring Web](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html) - Provides key web-related features, including multipart file upload functionality and initialization of the IoC container.
    - [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - Provides a simple and consistent programming model for data access.
    - [Spring Security](https://spring.io/projects/spring-security) - Provides comprehensive security services for Java EE-based enterprise software applications.
    - [Spring MVC](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html) - The web module of the Spring framework that simplifies the work needed to develop web applications.
  - [PostgreSQL](https://www.postgresql.org/) - An open-source relational database system.

- **Testing**:
  - [Mockito](https://site.mockito.org/) - Mockito is a mocking framework that tastes really good.
  - [JUnit 5](https://junit.org/junit5/) - A programming and testing model for Java applications.
  - [Testcontainers](https://www.testcontainers.org/) - Provides throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.
  - [Flyway](https://flywaydb.org/) - Database migration tool.
  
- **Development Tools**:
  - [Postman](https://www.postman.com/) - Postman is an API platform for building and using APIs.
  - [Lombok](https://projectlombok.org/) - A Java library that helps to reduce boilerplate code.

- **Containerization and Deployment**:
  - [Docker](https://www.docker.com/) - A platform for developing, shipping, and running applications.



## How to Install and Run the Project
### Prerequisites:

Java 11 or higher and Docker installed.

### Installing 
1. Clone the repo

```sh
git clone git@github.com:OlehTsipotan/Car-Rest-Microservice.git
```

### Running

1. Build executable Jar files
```sh
cd AuthServer
```
```sh
./mvnw package
```
---

```sh
cd ResourceServer
```
```sh
./mvnw package
```

2. Run throught Docker
```sh
docker-compose up
```
3. Have fun!
The application will be accessible at 
- http://localhost:8081 **Auth Server**
- http://localhost:8080 **Resource Server**

## Credits
Oleh Tsipotan - developer (https://www.linkedin.com/in/oleh-tsipotan/)
