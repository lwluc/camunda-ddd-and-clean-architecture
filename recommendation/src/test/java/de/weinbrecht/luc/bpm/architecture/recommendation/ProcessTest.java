package de.weinbrecht.luc.bpm.architecture.recommendation;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.extension.ZeebeProcessTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.ProcessTestUtils.*;
import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.*;
import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;

@ZeebeProcessTest
class ProcessTest {

    private ZeebeTestEngine engine;
    private ZeebeClient client;

    private static final String START_EVENT = "CrossSellingStartEvent";
    private static final String PICK_CONTENT_SERVICE_TASK = "PickContentServiceTask";
    private static final String SEND_RECOMMENDATION_SERVICE_TASK = "SendRecommendationServiceTask";
    private static final String END_EVENT = "CrossSellingRecommendationEndEvent";
    private static final String CONNECTOR_TOPIC = "de.weinbrecht.luc.bpm.architecture.sendcustomnotificationtaskhandler:1";

    @Test
    void testRunsProcess() throws Exception {
        deployResource(client, "cross_selling_recommendation.bpmn");

        final PublishMessageResponse response = sendMessage(engine, client, START_EVENT_MESSAGE_REF, "",
                Map.of(CUSTOMER_NUMBER, "A1"));

        assertThat(response).extractingProcessInstance()
                .hasPassedElement(START_EVENT)
                .isWaitingAtElements(PICK_CONTENT_SERVICE_TASK);

        completeTaskWithType(engine , client, PICK_CONTENT_SERVICE_TASK, PICK_CONTENT_TASK);

        assertThat(response).extractingProcessInstance()
                .hasPassedElementsInOrder(PICK_CONTENT_SERVICE_TASK)
                .isWaitingAtElements(SEND_RECOMMENDATION_SERVICE_TASK);

        completeTaskWithType(engine , client, SEND_RECOMMENDATION_SERVICE_TASK, CONNECTOR_TOPIC);

        assertThat(response).extractingProcessInstance()
                .hasPassedElementsInOrder(SEND_RECOMMENDATION_SERVICE_TASK, END_EVENT)
                .isCompleted();
    }
}
