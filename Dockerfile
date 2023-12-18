FROM eclipse-temurin:17-jdk-jammy
MAINTAINER Oleh Tsipotan
COPY target/resourceserver-0.1.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","resourceserver-0.1.jar"]