package de.weinbrecht.luc.bpm.architecture.notification.adapter.out.console;

import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Address;
import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.out.NotificationCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
class ConsoleNotificationCommandService implements NotificationCommand {

    @Override
    public void sendPushNotification(Address address, Content content) {
        log.info("Sending push notification to {} with content {}", address.getValue(), content.getValue());
    }
}
