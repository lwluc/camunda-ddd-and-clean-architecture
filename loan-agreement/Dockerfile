FROM openjdk:11-jre-slim-buster

COPY ./target/loan-agreement.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "loan-agreement.jar"]