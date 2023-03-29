package de.weinbrecht.luc.bpm.architecture.notification.domain.service;

import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Address;
import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.out.NotificationCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.Mockito.verify;

@MockitoSettings
class SendNotificationServiceTest {

    private SendNotificationService classUnderTest;

    @Mock
    private NotificationCommand notificationCommand;

    @BeforeEach
    void setUp() {
        classUnderTest = new SendNotificationService(notificationCommand);
    }

    @Test
    void should_call_out_use_case_and_send_notification() {
        final Address address = new Address("foo.bar@address");
        final Content content = new Content("Testing ...");

        classUnderTest.pushNotification(address, content);

        verify(notificationCommand).sendPushNotification(address, content);
    }
}