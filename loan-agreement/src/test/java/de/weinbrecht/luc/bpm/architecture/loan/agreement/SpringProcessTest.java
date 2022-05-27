package de.weinbrecht.luc.bpm.architecture.loan.agreement;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.*;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.MailAddress;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.Name;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.Recipient;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementCreation;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementStatusCommand;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementCommand;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.RecommendationTrigger;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.SpringProcessTestUtils.*;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.*;
import static io.camunda.zeebe.process.test.filters.RecordStream.of;
import static java.time.Duration.ofSeconds;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// https://github.com/camunda-community-hub/camunda-8-examples/blob/main/twitter-review-java-springboot/src/test/java/org/camunda/community/examples/twitter/TestTwitterProcess.java
@SpringBootTest
@ZeebeSpringTest
@Disabled("Flaky if runs together with the non Spring Process Tests")
@MockBean({LoanAgreementCommand.class, LoanAgreementCreation.class})
class SpringProcessTest {

    @Autowired
    private ZeebeClient zeebe;

    @Autowired
    private ZeebeTestEngine zeebeTestEngine;

    @MockBean
    private LoanAgreementStatusCommand loanAgreementStatusCommand;

    @MockBean
    private RecommendationTrigger recommendationTrigger;

    @MockBean
    private LoanAgreementQuery loanAgreementQuery;

    public static final String PROCESS_DEFINITION = "Loan_Agreement";
    private static final String START_EVENT = "LoanAgreementReceivedStartEvent";
    private static final String APPROVE_RULE_TASK = "ApproveAgreementRuleTask";
    private static final String APPROVE_AGREEMENT_SERVICE_TASK = "ApproveLoanAgreementServiceTask";
    private static final String SEND_CROSS_SELLING_EVENT = "SendCrossSellingEvent";
    private static final String APPROVED_END_EVENT = "LoanAgreementApprovedEndEvent";

    private static final String REJECT_AGREEMENT_SERVICE_TASK = "RejectLoanAgreementServiceTask";
    private static final String NOT_APPROVED_END_EVENT = "LoanAgreementNotApprovedEndEvent";

    @Test
    void testRunsProcessHappyPath() throws Exception {
        final LoanAgreementNumber loanAgreementNumber = new LoanAgreementNumber(1L);
        final CaseId caseId = new CaseId("Test-A-B");
        LoanAgreement loanAgreement = new LoanAgreement(
                loanAgreementNumber,
                new Recipient(
                        new CustomerNumber("Test-1"),
                        new Name("Tester"),
                        new MailAddress("tester@web.io")
                ),
                new Amount(100)
        );
        when(loanAgreementQuery.loadByNumber(loanAgreementNumber)).thenReturn(loanAgreement);

        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put(LOAN_AGREEMENT_NUMBER, loanAgreementNumber.getValue());
        processVariables.put(BUSINESS_KEY, caseId.getValue());
        zeebe.newPublishMessageCommand().messageName(LOAN_START_EVENT_MESSAGE_REF)
                .correlationKey("")
                .variables(processVariables)
                .send()
                .join();

        hasPassedElement(START_EVENT, of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION);

        zeebeTestEngine.waitForIdleState(ofSeconds(5));
        hasPassedElement(APPROVE_RULE_TASK, of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION);

        waitForTaskAndComplete(zeebeTestEngine, zeebe, APPROVE_AGREEMENT_SERVICE_TASK, LOAN_AGREEMENT_TASK);
        zeebeTestEngine.waitForIdleState(ofSeconds(5));
        verify(loanAgreementStatusCommand).accept(loanAgreementNumber);

        waitForTaskAndComplete(zeebeTestEngine, zeebe, SEND_CROSS_SELLING_EVENT, SEND_CROSS_SELLING_RECOMMENDATION_TASK);
        verify(recommendationTrigger).startLoanAgreement(caseId, loanAgreement);

        hasPassedElement(APPROVED_END_EVENT, of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION);

        assertTrue(isProcessInstanceCompleted(of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION));
    }

    @Test
    void testRunsProcessExceptionalPath() throws Exception {
        final LoanAgreementNumber loanAgreementNumber = new LoanAgreementNumber(6L);

        zeebe.newPublishMessageCommand().messageName(LOAN_START_EVENT_MESSAGE_REF)
                .correlationKey("")
                .variables(singletonMap(LOAN_AGREEMENT_NUMBER, loanAgreementNumber.getValue()))
                .send()
                .join();
        zeebeTestEngine.waitForIdleState(ofSeconds(5));

        hasPassedElement(START_EVENT, of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION);

        zeebeTestEngine.waitForIdleState(ofSeconds(5));
        hasPassedElement(APPROVE_RULE_TASK, of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION);

        waitForTaskAndComplete(zeebeTestEngine, zeebe, REJECT_AGREEMENT_SERVICE_TASK, LOAN_REJECTION_TASK);
        verify(loanAgreementStatusCommand).reject(loanAgreementNumber);

        zeebeTestEngine.waitForIdleState(ofSeconds(5));
        hasPassedElement(NOT_APPROVED_END_EVENT, of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION);

        assertTrue(isProcessInstanceCompleted(of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION));
    }
}
