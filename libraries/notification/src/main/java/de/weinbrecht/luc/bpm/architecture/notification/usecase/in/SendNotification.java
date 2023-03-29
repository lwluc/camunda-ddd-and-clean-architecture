package de.weinbrecht.luc.bpm.architecture.notification.usecase.in;

import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Address;
import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Content;

public interface SendNotification {
    void pushNotification(Address address, Content content);
}
