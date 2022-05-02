package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import org.camunda.bpm.engine.RuntimeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.START_EVENT_MESSAGE_REF;
import static org.mockito.Mockito.verify;

@MockitoSettings
class ProcessEngineClientTest {

    @InjectMocks
    private ProcessEngineClient classUnderTest;

    @Mock
    private RuntimeService runtimeService;

    private final CaseId caseId = new CaseId("11");
    private final LoanAgreementNumber loanAgreementNumber = new LoanAgreementNumber(1L);

    @Test
    void should_class_runtime_service_to_start() {
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put(LOAN_AGREEMENT_NUMBER, loanAgreementNumber.getValue());

        classUnderTest.startLoanAgreement(caseId, loanAgreementNumber);

        verify(runtimeService).startProcessInstanceByMessage(START_EVENT_MESSAGE_REF, caseId.getValue(), processVariables);
    }
}