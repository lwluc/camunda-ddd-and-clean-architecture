package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementStatusCommand;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static org.mockito.Mockito.*;

@MockitoSettings
class RejectionLoanAgreementTest {

    @InjectMocks
    private RejectionLoanAgreement classUnderTest;

    @Mock
    private LoanAgreementStatusCommand loanAgreementStatusCommand;

    @Test
    void should_call_command_and_reject() {
        LoanAgreementNumber loanAgreementNumber = new LoanAgreementNumber(1L);
        JobClient client = mock(JobClient.class, RETURNS_DEEP_STUBS);
        ActivatedJob job = mock(ActivatedJob.class);
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put(LOAN_AGREEMENT_NUMBER, loanAgreementNumber.getValue());
        when(job.getVariablesAsMap()).thenReturn(processVariables);
        when(job.getKey()).thenReturn(1L);

        classUnderTest.handleJobFoo(client, job);

        verify(loanAgreementStatusCommand).reject(loanAgreementNumber);
        verify(client.newCompleteCommand(job.getKey())).send();
    }
}