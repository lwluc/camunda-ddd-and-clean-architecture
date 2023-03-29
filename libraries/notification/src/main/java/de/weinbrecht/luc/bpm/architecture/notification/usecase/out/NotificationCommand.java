package de.weinbrecht.luc.bpm.architecture.notification.usecase.out;

import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Address;
import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Content;

public interface NotificationCommand {
    void sendPushNotification(Address address, Content content);
}
