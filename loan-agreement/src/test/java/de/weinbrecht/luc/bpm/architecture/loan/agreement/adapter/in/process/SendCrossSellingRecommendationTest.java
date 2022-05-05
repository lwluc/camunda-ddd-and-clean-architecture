package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.RecommendationTrigger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.TestdataGenerator.createLoanAgreementWithNumber;
import static org.mockito.Mockito.*;

@MockitoSettings
class SendCrossSellingRecommendationTest {

    @InjectMocks
    private SendCrossSellingRecommendation classUnderTest;

    @Mock
    private RecommendationTrigger recommendationTrigger;

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

        verify(recommendationTrigger).startLoanAgreement(new CaseId(caseId), loanAgreement);
    }
}