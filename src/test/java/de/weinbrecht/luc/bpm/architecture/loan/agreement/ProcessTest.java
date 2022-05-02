package de.weinbrecht.luc.bpm.architecture.loan.agreement;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process.ApproveLoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process.RejectionLoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process.SendCrossSellingRecommendation;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.LoanAgreement.LOAN_AGREEMENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.LoanAgreement.PROCESS_DEFINITION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.decisionService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.execute;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.job;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;
import static org.camunda.community.mockito.DelegateExpressions.registerJavaDelegateMock;
import static org.camunda.community.mockito.DelegateExpressions.verifyJavaDelegateMock;

@ExtendWith(ProcessEngineExtension.class)
class ProcessTest {

    private static final String START_EVENT = "LoanAgreementReciedStartEvent";
    private static final String APPROVE_RULE_TASK = "ApproveAgreementRuleTask";
    private static final String APPROVE_AGREEMENT_SERVICE_TASK = "ApproveLoanAgreementServiceTask";
    private static final String SEND_CROSS_SELLING_EVENT = "SendCrossSellingEvent";
    private static final String APPROVED_END_EVENT = "LoanAgreementApprovedEndEvent";

    private static final String REJECT_AGREEMENT_SERVICE_TASK = "RejectLoanAgreementServiceTask";
    private static final String NOT_APPROVED_END_EVENT = "LoanAgreementNotApprovedEndEvent";

    private static final String DMN_DEFINITION = "approvement-check";

    @BeforeEach
    void setUp() {
        registerJavaDelegateMock(ApproveLoanAgreement.class);
        registerJavaDelegateMock(RejectionLoanAgreement.class);
        registerJavaDelegateMock(SendCrossSellingRecommendation.class);
    }

    @Test
    @Deployment(resources = { "loan_agreement.bpmn", "approve_agreement.dmn"})
    void shouldExecuteProcess_happy_path() {
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
                PROCESS_DEFINITION,
                withVariables(LOAN_AGREEMENT_NUMBER, 1L)
        );
        assertThat(processInstance).isActive();

        assertThat(processInstance).isWaitingAt(START_EVENT);

        execute(job());

        assertThat(processInstance)
                .hasPassed(START_EVENT,
                        APPROVE_RULE_TASK,
                        APPROVE_AGREEMENT_SERVICE_TASK,
                        SEND_CROSS_SELLING_EVENT,
                        APPROVED_END_EVENT);

        verifyJavaDelegateMock(SendCrossSellingRecommendation.class).executed();
        verifyJavaDelegateMock(ApproveLoanAgreement.class).executed();

        assertThat(processInstance).isEnded();
    }

    @Test
    @Deployment(resources = { "loan_agreement.bpmn", "approve_agreement.dmn"})
    void shouldExecuteProcess_exceptional_path() {
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
                PROCESS_DEFINITION,
                withVariables(LOAN_AGREEMENT_NUMBER, 8L)
        );
        assertThat(processInstance).isActive();

        assertThat(processInstance).isWaitingAt(START_EVENT);

        execute(job());

        assertThat(processInstance)
                .hasPassed(START_EVENT,
                        APPROVE_RULE_TASK,
                        REJECT_AGREEMENT_SERVICE_TASK,
                        NOT_APPROVED_END_EVENT);

        verifyJavaDelegateMock(RejectionLoanAgreement.class).executed();

        assertThat(processInstance).isEnded();
    }

    @ParameterizedTest
    @MethodSource("provideProcessVariablesForDMN")
    @Deployment(resources = "approve_agreement.dmn")
    void testTweetApprovalIBM(Long input, boolean expected) {
        Map<String, Object> variables = withVariables(LOAN_AGREEMENT_NUMBER, input);

        DmnDecisionTableResult tableResult = decisionService().evaluateDecisionTableByKey(DMN_DEFINITION, variables);

        assertThat(tableResult.getFirstResult()).contains(entry("approved", expected));
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
