package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import io.camunda.zeebe.client.ZeebeClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;

@MockitoSettings
class ProcessEngineClientTest {

    @InjectMocks
    private ProcessEngineClient classUnderTest;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ZeebeClient client;

    private final CaseId caseId = new CaseId("11");
    private final LoanAgreementNumber loanAgreementNumber = new LoanAgreementNumber(1L);

    @Test
    void should_call_zeebe_to_start_reommendation() {
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put(LOAN_AGREEMENT_NUMBER, loanAgreementNumber.getValue());
        processVariables.put(BUSINESS_KEY, caseId.getValue());

        classUnderTest.startLoanAgreement(caseId, loanAgreementNumber);

        verify(client
                .newPublishMessageCommand()
                .messageName(LOAN_START_EVENT_MESSAGE_REF)
                .correlationKey("")
                .variables(processVariables)
        ).send();
    }
}