package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.process;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import io.camunda.zeebe.client.ZeebeClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.CUSTOMER_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.START_EVENT_MESSAGE_REF;
import static java.util.Collections.singletonMap;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;

@MockitoSettings
class ProcessEngineClientTest {

    @InjectMocks
    private ProcessEngineClient classUnderTest;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ZeebeClient zeebeClient;

    @Test
    void should_class_runtime_service_to_start() {
        String caseId = "super-test";
        CustomerId customerId = new CustomerId("Test");

        classUnderTest.start(caseId, customerId);

        verify(zeebeClient
                .newPublishMessageCommand()
                .messageName(START_EVENT_MESSAGE_REF)
                .correlationKey("")
                .variables(singletonMap(CUSTOMER_NUMBER, customerId.getValue()))
        ).send();
    }
}