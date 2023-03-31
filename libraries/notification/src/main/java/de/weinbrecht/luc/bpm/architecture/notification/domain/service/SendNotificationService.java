package de.weinbrecht.luc.bpm.architecture.notification.domain.service;

import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Address;
import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.in.SendNotification;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.out.NotificationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class SendNotificationService implements SendNotification {

    private final NotificationCommand notificationCommand;

    @Override
    public void pushNotification(Address address, Content content) {
        notificationCommand.sendPushNotification(address, content);
    }
}
