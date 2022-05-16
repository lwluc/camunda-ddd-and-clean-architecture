package de.weinbrecht.luc.bpm.architecture.recommendation;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.extension.ZeebeProcessTest;
import org.junit.jupiter.api.Test;

import static de.weinbrecht.luc.bpm.architecture.recommendation.ProcessTestUtils.*;
import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.*;
import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;

@ZeebeProcessTest
class ProcessTestZeebe {

    private ZeebeTestEngine engine;
    private ZeebeClient client;

    private static final String START_EVENT = "CrossSellingStartEvent";
    private static final String PICK_CONTENT_SERVICE_TASK = "PickContentServiceTask";
    private static final String SEND_RECOMMENDATION_SERVICE_TASK = "SendRecommendationServiceTask";
    private static final String END_EVENT = "CrossSellingRecommendationEndEvent";

    @Test
    void testRunsProcess() throws Exception {
        deployResource(client, "cross_selling_recommendation.bpmn");

        final PublishMessageResponse response = sendMessage(engine, client, START_EVENT_MESSAGE_REF, "");

        assertThat(response).extractingProcessInstance()
                .hasPassedElement(START_EVENT)
                .isWaitingAtElements(PICK_CONTENT_SERVICE_TASK);

        completeTaskWithType(engine , client, PICK_CONTENT_SERVICE_TASK, PICK_CONTENT_TASK);

        assertThat(response).extractingProcessInstance()
                .hasPassedElement(PICK_CONTENT_SERVICE_TASK)
                .isWaitingAtElements(SEND_RECOMMENDATION_SERVICE_TASK);

        completeTaskWithType(engine , client, SEND_RECOMMENDATION_SERVICE_TASK, SEND_RECOMMENDATION_TASK);

        assertThat(response).extractingProcessInstance()
                .hasPassedElementsInOrder(SEND_RECOMMENDATION_SERVICE_TASK, END_EVENT)
                .isCompleted();
    }
}
