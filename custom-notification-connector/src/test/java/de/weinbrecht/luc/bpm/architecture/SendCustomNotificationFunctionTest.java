package de.weinbrecht.luc.bpm.architecture;

import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Address;
import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.in.SendNotification;
import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.Mockito.*;

@MockitoSettings
class SendCustomNotificationFunctionTest {

    private SendCustomNotificationFunction classUnderTest;

    @Mock
    private SendNotification sendNotificationMock;

    @BeforeEach
    void setUp() {
        classUnderTest = new SendCustomNotificationFunction(sendNotificationMock);
    }

    @Test
    void should_read_variables_and_send_notification() throws Exception {
        final Address address = new Address("foo.bar@test");
        final Content content = new Content("testing ...");
        var input = new NotificationInput();
        input.setNotificationAddress(address.getValue());
        input.setNotificationContent(content.getValue());
        var context = OutboundConnectorContextBuilder.create()
                .variables(input)
                .build();

        classUnderTest.execute(context);

        verify(sendNotificationMock).pushNotification(address, content);
    }

    @Test
    void should_catch_error_and_not_complete_task_on_error() throws Exception {
        final Address address = new Address("foo.bar@test");
        final Content content = new Content("testing ...");
        var input = new NotificationInput();
        input.setNotificationAddress(address.getValue());
        input.setNotificationContent(content.getValue());
        var context = OutboundConnectorContextBuilder.create()
                .variables(input)
                .build();
        doThrow(RuntimeException.class).when(sendNotificationMock).pushNotification(any(), any());

        classUnderTest.execute(context);
    }
}