FROM maven:3.8.5-openjdk-17 as build

COPY ./pom.xml .

RUN mvn -B dependency:go-offline

COPY ./src src

RUN mvn -B package

FROM openjdk:11-jre-slim-buster

COPY --from=build ./target/loan-agreement.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "loan-agreement.jar"]