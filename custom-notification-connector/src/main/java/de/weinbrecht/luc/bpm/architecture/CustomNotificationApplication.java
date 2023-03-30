package de.weinbrecht.luc.bpm.architecture;

import de.weinbrecht.luc.bpm.architecture.notification.EnableNotification;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableNotification
public class CustomNotificationApplication {
    public static void main(String... args) {
        SpringApplication.run(CustomNotificationApplication.class, args);
    }
}