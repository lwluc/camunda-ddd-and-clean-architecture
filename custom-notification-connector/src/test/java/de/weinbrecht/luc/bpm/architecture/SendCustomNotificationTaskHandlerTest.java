package de.weinbrecht.luc.bpm.architecture;

import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Address;
import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.in.SendNotification;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.task.impl.ExternalTaskImpl;
import org.camunda.bpm.client.variable.impl.TypedValueField;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.camunda.community.mockito.task.LockedExternalTaskFake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.SendCustomNotificationTaskHandler.ADDRESS_INPUT_NAME;
import static de.weinbrecht.luc.bpm.architecture.SendCustomNotificationTaskHandler.CONTENT_INPUT_NAME;
import static org.mockito.Mockito.*;

@MockitoSettings
class SendCustomNotificationTaskHandlerTest {

    private SendCustomNotificationTaskHandler classUnderTest;

    @Mock
    private SendNotification sendNotificationMock;

    @BeforeEach
    void setUp() {
        classUnderTest = new SendCustomNotificationTaskHandler(sendNotificationMock);
    }

    @Test
    void should_read_variables_and_send_notification() {
        final Address address = new Address("foo.bar@test");
        final Content content = new Content("testing ...");
        ExternalTask externalTaskMock = mock(ExternalTask.class);
        when(externalTaskMock.getVariable(ADDRESS_INPUT_NAME)).thenReturn(address.getValue());
        when(externalTaskMock.getVariable(CONTENT_INPUT_NAME)).thenReturn(content.getValue());
        ExternalTaskService externalTaskServiceMock = mock(ExternalTaskService.class);

        classUnderTest.execute(externalTaskMock, externalTaskServiceMock);

        verify(sendNotificationMock).pushNotification(address, content);
        verify(externalTaskServiceMock).complete(externalTaskMock);
    }

    @Test
    void should_catch_error_and_not_complete_task_on_error() {
        final Address address = new Address("foo.bar@test");
        final Content content = new Content("testing ...");
        ExternalTask externalTaskMock = mock(ExternalTask.class);
        ExternalTaskService externalTaskServiceMock = mock(ExternalTaskService.class);
        when(externalTaskMock.getVariable(ADDRESS_INPUT_NAME)).thenReturn(address.getValue());
        when(externalTaskMock.getVariable(CONTENT_INPUT_NAME)).thenReturn(content.getValue());
        doThrow(RuntimeException.class).when(sendNotificationMock).pushNotification(any(), any());

        classUnderTest.execute(externalTaskMock, externalTaskServiceMock);

        verifyNoInteractions(externalTaskServiceMock);
    }
}