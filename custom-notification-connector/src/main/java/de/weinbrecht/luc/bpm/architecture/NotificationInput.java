package de.weinbrecht.luc.bpm.architecture;

import lombok.Data;

@Data
public class NotificationInput {

    private String notificationAddress;
    private String notificationContent;
}
