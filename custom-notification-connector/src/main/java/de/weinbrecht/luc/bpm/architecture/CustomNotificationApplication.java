package de.weinbrecht.luc.bpm.architecture;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient
public class CustomNotificationApplication {
    public static void main(String... args) {
        SpringApplication.run(CustomNotificationApplication.class, args);
    }
}