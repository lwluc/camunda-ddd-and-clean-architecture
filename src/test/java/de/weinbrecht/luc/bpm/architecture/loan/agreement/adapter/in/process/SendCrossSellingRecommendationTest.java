package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.CrossSellingRecommendation.CUSTOMER_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.CrossSellingRecommendation.START_EVENT_MESSAGE_REF;
import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.LoanAgreement.LOAN_AGREEMENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.TestdataGenerator.createLoanAgreementWithNumber;
import static org.mockito.Mockito.*;

@MockitoSettings
class SendCrossSellingRecommendationTest {

    @InjectMocks
    private SendCrossSellingRecommendation classUnderTest;

    @Mock
    private RuntimeService runtimeService;

    @Mock
    LoanAgreementQuery loanAgreementQuery;

    @Test
    void should_load_data_and_start_process_by_message() {
        LoanAgreement loanAgreement = createLoanAgreementWithNumber();
        String caseId = "11";
        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        when(delegateExecution.getBusinessKey()).thenReturn(caseId);
        when(delegateExecution.getVariable(LOAN_AGREEMENT_NUMBER))
                .thenReturn(loanAgreement.getLoanAgreementNumber().getValue());
        when(loanAgreementQuery.loadByNumber(loanAgreement.getLoanAgreementNumber())).thenReturn(loanAgreement);

        classUnderTest.execute(delegateExecution);

        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put(CUSTOMER_NUMBER, loanAgreement.getRecipient().getCustomerNumber().getValue());
        verify(runtimeService).startProcessInstanceByMessage(
                START_EVENT_MESSAGE_REF,
                caseId + "-" + loanAgreement.getLoanAgreementNumber().getValue(),
                processVariables
        );
    }
}