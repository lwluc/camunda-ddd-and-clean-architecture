package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.RecommendationTrigger;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.BUSINESS_KEY;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.TestdataGenerator.createLoanAgreementWithNumber;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
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
        CaseId caseId = new CaseId("11");
        JobClient client = mock(JobClient.class, RETURNS_DEEP_STUBS);
        ActivatedJob job = mock(ActivatedJob.class);
        Map<String, Object> variables = new HashMap<>();
        variables.put(LOAN_AGREEMENT_NUMBER, loanAgreement.getLoanAgreementNumber().getValue().intValue());
        variables.put(BUSINESS_KEY, caseId.getValue());
        when(job.getVariablesAsMap()).thenReturn(variables);
        when(job.getKey()).thenReturn(1L);
        when(loanAgreementQuery.loadByNumber(loanAgreement.getLoanAgreementNumber())).thenReturn(loanAgreement);

        classUnderTest.handleJobFoo(client, job);

        verify(client.newCompleteCommand(job.getKey())).send();

        verify(recommendationTrigger).startLoanAgreement(caseId, loanAgreement);
    }
}