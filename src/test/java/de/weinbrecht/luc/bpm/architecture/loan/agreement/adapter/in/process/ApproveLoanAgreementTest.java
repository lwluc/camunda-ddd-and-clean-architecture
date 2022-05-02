package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementStatusCommand;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static org.mockito.Mockito.*;

@MockitoSettings
class ApproveLoanAgreementTest {
    @InjectMocks
    private ApproveLoanAgreement classUnderTest;

    @Mock
    private LoanAgreementStatusCommand loanAgreementStatusCommand;

    @Test
    void should_call_command_and_reject() {
        LoanAgreementNumber loanAgreementNumber = new LoanAgreementNumber(1L);
        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        when(delegateExecution.getVariable(LOAN_AGREEMENT_NUMBER)).thenReturn(loanAgreementNumber.getValue());

        classUnderTest.execute(delegateExecution);

        verify(loanAgreementStatusCommand).accept(loanAgreementNumber);
    }
}