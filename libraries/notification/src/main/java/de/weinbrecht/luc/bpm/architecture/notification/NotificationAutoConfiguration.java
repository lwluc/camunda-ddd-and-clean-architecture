package de.weinbrecht.luc.bpm.architecture.notification;

import de.weinbrecht.luc.bpm.architecture.notification.adapter.out.console.ConsoleNotificationCommandService;
import de.weinbrecht.luc.bpm.architecture.notification.domain.service.SendNotificationService;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.in.SendNotification;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.out.NotificationCommand;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(SendNotification.class)
public class NotificationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SendNotification sendNotification(NotificationCommand notificationCommand) {
        return new SendNotificationService(notificationCommand);
    }

    @Bean
    @ConditionalOnMissingBean
    public NotificationCommand notificationCommand() {
        return new ConsoleNotificationCommandService();
    }
}
