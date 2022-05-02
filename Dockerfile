FROM maven:3.8.5-openjdk-17 as build

COPY pom.xml .

RUN mvn -B dependency:go-offline

COPY src src

RUN mvn -B package

FROM openjdk:11-jre-slim-buster

COPY --from=build target/camunda-ddd-and-clean-architecture.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "camunda-ddd-and-clean-architecture.jar"]