package de.weinbrecht.luc.bpm.architecture.loan.agreement;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process.ApproveLoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process.RejectionLoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process.SendCrossSellingRecommendation;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static io.camunda.zeebe.protocol.Protocol.USER_TASK_JOB_TYPE;
import static io.camunda.zeebe.spring.test.ZeebeTestThreadSupport.waitForProcessInstanceCompleted;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

// Source: https://github.com/camunda-community-hub/camunda-cloud-examples/blob/main/twitter-review-java-springboot/src/test/java/org/camunda/community/examples/twitter/TestTwitterProcess.java
@Disabled
@SpringBootTest
@ZeebeSpringTest
class ProcessTest {

    public static final String PROCESS_DEFINITION = "Loan_Agreement";
    private static final String START_EVENT = "LoanAgreementReceivedStartEvent";
    private static final String APPROVE_RULE_TASK = "ApproveAgreementRuleTask";
    private static final String APPROVE_AGREEMENT_SERVICE_TASK = "ApproveLoanAgreementServiceTask";
    private static final String SEND_CROSS_SELLING_EVENT = "SendCrossSellingEvent";
    private static final String APPROVED_END_EVENT = "LoanAgreementApprovedEndEvent";

    private static final String REJECT_AGREEMENT_SERVICE_TASK = "RejectLoanAgreementServiceTask";
    private static final String NOT_APPROVED_END_EVENT = "LoanAgreementNotApprovedEndEvent";

    private static final String DMN_DEFINITION = "approvement-check";

    @Autowired
    private ZeebeClient zeebe;

    @Autowired
    private ZeebeTestEngine zeebeTestEngine;

    @MockBean
    private ApproveLoanAgreement approveLoanAgreement;

    @MockBean
    private RejectionLoanAgreement rejectionLoanAgreement;

    @MockBean
    private SendCrossSellingRecommendation sendCrossSellingRecommendation;

    @Test
    void shouldExecuteProcess_happy_path() {
        Map<String, Object> variables = new HashMap<>();
        variables.put(LOAN_AGREEMENT_NUMBER, 1L);
        ProcessInstanceEvent processInstance = zeebe.newCreateInstanceCommand()
                .bpmnProcessId(PROCESS_DEFINITION).latestVersion()
                .variables(variables)
                .send().join();

        assertThat(processInstance).isActive();

        assertThat(processInstance).isWaitingAtElements(START_EVENT);

        // TODO: complete async tasks

        assertThat(processInstance).hasPassedElementsInOrder(
                START_EVENT,
                APPROVE_RULE_TASK,
                APPROVE_AGREEMENT_SERVICE_TASK,
                SEND_CROSS_SELLING_EVENT,
                APPROVED_END_EVENT);

        verify(approveLoanAgreement).handleJobFoo(any(), any());
        verify(sendCrossSellingRecommendation).handleJobFoo(any(), any());

        waitForProcessInstanceCompleted(processInstance);

        assertThat(processInstance).isCompleted();
    }

//    @Test
//    void shouldExecuteProcess_exceptional_path() {
//        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
//                PROCESS_DEFINITION,
//                withVariables(LOAN_AGREEMENT_NUMBER, 8L)
//        );
//        assertThat(processInstance).isActive();
//
//        assertThat(processInstance).isWaitingAt(START_EVENT);
//
//        execute(job());
//
//        assertThat(processInstance)
//                .hasPassed(START_EVENT,
//                        APPROVE_RULE_TASK,
//                        REJECT_AGREEMENT_SERVICE_TASK);
//
//        verifyJavaDelegateMock(RejectionLoanAgreement.class).executed();
//
//        assertThat(processInstance).isEnded();
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideProcessVariablesForDMN")
//    void testTweetApprovalIBM(Long input, boolean expected) {
//        Map<String, Object> variables = withVariables(LOAN_AGREEMENT_NUMBER, input);
//
//        DmnDecisionTableResult tableResult = decisionService().evaluateDecisionTableByKey(DMN_DEFINITION, variables);
//
//        assertThat(tableResult.getFirstResult()).contains(entry("approved", expected));
//    }


    private void waitForUserTaskAndComplete(String userTaskId, Map<String, Object> variables) throws Exception {
        // Let the workflow engine do whatever it needs to do
        zeebeTestEngine.waitForIdleState(Duration.ofSeconds(10));

        // Now get all user tasks
        List<ActivatedJob> jobs = zeebe.newActivateJobsCommand().jobType(USER_TASK_JOB_TYPE).maxJobsToActivate(1).workerName("waitForUserTaskAndComplete").send().join().getJobs();

        // Should be only one
        assertTrue(jobs.size()>0, "Job for user task '" + userTaskId + "' does not exist");
        ActivatedJob userTaskJob = jobs.get(0);
        // Make sure it is the right one
        if (userTaskId!=null) {
            assertEquals(userTaskId, userTaskJob.getElementId());
        }

        // And complete it passing the variables
        if (variables!=null && variables.size()>0) {
            zeebe.newCompleteCommand(userTaskJob.getKey()).variables(variables).send().join();
        } else {
            zeebe.newCompleteCommand(userTaskJob.getKey()).send().join();
        }
    }

    private static Stream<Arguments> provideProcessVariablesForDMN() {
        return Stream.of(
                Arguments.of(1L, true),
                Arguments.of(2L, true),
                Arguments.of(3L, true),
                Arguments.of(4L, true),
                Arguments.of(5L, false),
                Arguments.of(6L, false)
        );
    }
}
