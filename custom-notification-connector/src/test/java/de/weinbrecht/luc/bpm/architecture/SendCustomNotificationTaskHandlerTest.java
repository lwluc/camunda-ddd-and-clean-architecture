package de.weinbrecht.luc.bpm.architecture;

import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Address;
import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.in.SendNotification;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.ZeebeFuture;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.SendCustomNotificationTaskHandler.ADDRESS_INPUT_NAME;
import static de.weinbrecht.luc.bpm.architecture.SendCustomNotificationTaskHandler.CONTENT_INPUT_NAME;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;

@MockitoSettings
class SendCustomNotificationTaskHandlerTest {

    private SendCustomNotificationTaskHandler classUnderTest;

    @Mock
    private SendNotification sendNotificationMock;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ZeebeClient zeebeClientMock;

    @BeforeEach
    void setUp() {
        classUnderTest = new SendCustomNotificationTaskHandler(sendNotificationMock, zeebeClientMock);
    }

    @Test
    void should_read_variables_and_send_notification() {
        final Address address = new Address("foo.bar@test");
        final Content content = new Content("testing ...");
        ActivatedJob jobMock = mock(ActivatedJob.class, Mockito.RETURNS_DEEP_STUBS);
        when(jobMock.getVariablesAsMap()).thenReturn(Map.of(
                ADDRESS_INPUT_NAME, address.getValue(),
                CONTENT_INPUT_NAME, content.getValue()
        ));
        when(zeebeClientMock.newCompleteCommand(jobMock)
                .send())
                .thenReturn(mock(ZeebeFuture.class));

        classUnderTest.execute(jobMock);

        verify(sendNotificationMock).pushNotification(address, content);
        verify(zeebeClientMock
                .newCompleteCommand(jobMock)
        ).send();
    }

    @Test
    void should_catch_error_and_not_complete_task_on_error() {
        final Address address = new Address("foo.bar@test");
        final Content content = new Content("testing ...");
        ActivatedJob jobMock = mock(ActivatedJob.class, Mockito.RETURNS_DEEP_STUBS);
        when(jobMock.getVariablesAsMap()).thenReturn(Map.of(
                ADDRESS_INPUT_NAME, address.getValue(),
                CONTENT_INPUT_NAME, content.getValue()
        ));
        doThrow(RuntimeException.class).when(sendNotificationMock).pushNotification(any(), any());

        classUnderTest.execute(jobMock);

        verifyNoInteractions(zeebeClientMock);
    }
}