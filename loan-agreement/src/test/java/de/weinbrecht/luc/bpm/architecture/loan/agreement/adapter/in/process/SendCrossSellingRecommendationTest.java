package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.RecommendationTrigger;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.TestdataGenerator.createLoanAgreementWithNumber;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        CaseId caseId = new CaseId("11");
        when(loanAgreementQuery.loadByNumber(loanAgreement.getLoanAgreementNumber())).thenReturn(loanAgreement);

        classUnderTest.handleJobFoo(loanAgreement.getLoanAgreementNumber().getValue(), caseId.getValue());

        verify(recommendationTrigger).startLoanAgreement(caseId, loanAgreement);
    }
}