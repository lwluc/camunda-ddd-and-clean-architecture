FROM maven:3.8.5-openjdk-17 as build

COPY ./pom.xml .

RUN mvn -B dependency:go-offline

COPY ./src src

RUN mvn -B package

FROM openjdk:11-jre-slim-buster

COPY --from=build ../target/recommendation.jar .

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "recommendation.jar"]