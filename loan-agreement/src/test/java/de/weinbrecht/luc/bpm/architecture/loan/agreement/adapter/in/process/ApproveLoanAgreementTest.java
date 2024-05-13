package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementStatusCommand;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.Mockito.verify;

@MockitoSettings
class ApproveLoanAgreementTest {
    @InjectMocks
    private ApproveLoanAgreement classUnderTest;

    @Mock
    private LoanAgreementStatusCommand loanAgreementStatusCommand;

    @Test
    void should_call_command_and_reject() {
        LoanAgreementNumber loanAgreementNumber = new LoanAgreementNumber(1L);

        classUnderTest.handleJobFoo(loanAgreementNumber.getValue());

        verify(loanAgreementStatusCommand).accept(loanAgreementNumber);
    }
}