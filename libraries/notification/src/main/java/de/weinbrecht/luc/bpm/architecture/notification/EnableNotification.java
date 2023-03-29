package de.weinbrecht.luc.bpm.architecture.notification;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(NotificationConfiguration.class)
@Configuration
public @interface EnableNotification {
}
