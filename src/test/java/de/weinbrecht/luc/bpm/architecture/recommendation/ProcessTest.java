package de.weinbrecht.luc.bpm.architecture.recommendation;

import de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.process.PickContent;
import de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.process.SendRecommendation;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.CrossSellingRecommendation.PROCESS_DEFINITION;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.community.mockito.DelegateExpressions.registerJavaDelegateMock;
import static org.camunda.community.mockito.DelegateExpressions.verifyJavaDelegateMock;

@ExtendWith(ProcessEngineExtension.class)
class ProcessTest {

    private static final String START_EVENT = "CrossSellingStartEvent";
    private static final String PICK_CONTENT_SERVICE_TASK = "PickContentServiceTask";
    private static final String SEND_RECOMMENDATION_SERVICE_TASK = "SendRecommendationServiceTask";
    private static final String END_EVENT = "CrossSellingRecommendationEndEvent";

    @BeforeEach
    void setUp() {
        registerJavaDelegateMock(PickContent.class);
        registerJavaDelegateMock(SendRecommendation.class);
    }

    @Test
    @Deployment(resources = "cross_selling_recommendation.bpmn")
    void shouldExecuteProcess_happy_path() {
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
                PROCESS_DEFINITION
        );

        assertThat(processInstance)
                .hasPassedInOrder(
                        START_EVENT,
                        PICK_CONTENT_SERVICE_TASK,
                        SEND_RECOMMENDATION_SERVICE_TASK,
                        END_EVENT);

        verifyJavaDelegateMock(PickContent.class).executed();
        verifyJavaDelegateMock(SendRecommendation.class).executed();

        assertThat(processInstance).isEnded();
    }
}
