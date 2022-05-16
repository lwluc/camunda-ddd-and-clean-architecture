package de.weinbrecht.luc.bpm.architecture.loan.agreement;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.extension.ZeebeProcessTest;
import org.junit.jupiter.api.Test;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.ProcessTestUtils.*;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.*;
import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static java.util.Collections.singletonMap;

@ZeebeProcessTest
class ProcessTestZeebe {

    private ZeebeTestEngine engine;
    private ZeebeClient client;

    private static final String START_EVENT = "LoanAgreementReceivedStartEvent";
    private static final String APPROVE_RULE_TASK = "ApproveAgreementRuleTask";
    private static final String APPROVE_AGREEMENT_SERVICE_TASK = "ApproveLoanAgreementServiceTask";
    private static final String SEND_CROSS_SELLING_EVENT = "SendCrossSellingEvent";
    private static final String APPROVED_END_EVENT = "LoanAgreementApprovedEndEvent";

    private static final String REJECT_AGREEMENT_SERVICE_TASK = "RejectLoanAgreementServiceTask";
    private static final String NOT_APPROVED_END_EVENT = "LoanAgreementNotApprovedEndEvent";

    @Test
    void testRunsHappyPath() throws Exception {
        deployResources(client, "loan_agreement.bpmn", "approve_agreement.dmn");

        final PublishMessageResponse response = sendMessage(engine, client,
                LOAN_START_EVENT_MESSAGE_REF, "", singletonMap(LOAN_AGREEMENT_NUMBER, 1L));

        assertThat(response).extractingProcessInstance()
                .hasPassedElementsInOrder(START_EVENT, APPROVE_RULE_TASK)
                .isWaitingAtElements(APPROVE_AGREEMENT_SERVICE_TASK);

        completeTaskWithType(engine , client, APPROVE_AGREEMENT_SERVICE_TASK, LOAN_AGREEMENT_TASK);

        assertThat(response).extractingProcessInstance()
                .hasPassedElement(START_EVENT)
                .isWaitingAtElements(SEND_CROSS_SELLING_EVENT);

        completeTaskWithType(engine , client, SEND_CROSS_SELLING_EVENT, SEND_CROSS_SELLING_RECOMMENDATION_TASK);

        assertThat(response).extractingProcessInstance()
                .hasPassedElementsInOrder(SEND_CROSS_SELLING_EVENT, APPROVED_END_EVENT)
                .isCompleted();
    }

    @Test
    void testRunsExceptionPath() throws Exception {
        deployResources(client, "loan_agreement.bpmn", "approve_agreement.dmn");

        final PublishMessageResponse response = sendMessage(engine, client,
                LOAN_START_EVENT_MESSAGE_REF, "", singletonMap(LOAN_AGREEMENT_NUMBER, 6L));

        assertThat(response).extractingProcessInstance()
                .hasPassedElementsInOrder(START_EVENT, APPROVE_RULE_TASK)
                .isWaitingAtElements(REJECT_AGREEMENT_SERVICE_TASK);

        completeTaskWithType(engine , client, REJECT_AGREEMENT_SERVICE_TASK, LOAN_REJECTION_TASK);

        assertThat(response).extractingProcessInstance()
                .hasPassedElementsInOrder(NOT_APPROVED_END_EVENT)
                .hasNotPassedElement(SEND_CROSS_SELLING_EVENT)
                .isCompleted();
    }
}
