FROM openjdk:11-jre-slim-buster

COPY ./target/recommendation.jar .

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "recommendation.jar"]