package de.weinbrecht.luc.bpm.architecture.loan.agreement;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.service.LoanAgreementException;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.service.LoanAgreementService;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementCommand;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementDistributor;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.ProcessEngineCommand;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.TestdataGenerator.createLoanAgreementWithNumber;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@MockitoSettings
class LoanAgreementServiceTest {

    @InjectMocks
    private LoanAgreementService classUnderTest;

    @Mock
    private LoanAgreementCommand loanAgreementCommand;

    @Mock
    private ProcessEngineCommand processEngineCommand;

    @Mock
    private LoanAgreementDistributor loanAgreementDistributor;

    @Mock
    private LoanAgreementQuery loanAgreementQuery;

    @Test
    void should_safe_and_set_loan_number_on_creation() {
        LoanAgreement loanAgreement = createLoanAgreementWithNumber();
        when(loanAgreementCommand.save(loanAgreement)).thenReturn(loanAgreement.getLoanAgreementNumber());
        CaseId caseId = new CaseId("12");

        classUnderTest.create(loanAgreement, caseId);

        verify(processEngineCommand).startLoanAgreement(caseId, loanAgreement.getLoanAgreementNumber());
    }

    @Test
    void should_catch_exception_and_throw_custom_one_on_creation() {
        LoanAgreement loanAgreement = createLoanAgreementWithNumber();
        CaseId caseId = new CaseId("12");
        when(loanAgreementCommand.save(loanAgreement)).thenThrow(RuntimeException.class);

        assertThrows(LoanAgreementException.class,
                () -> classUnderTest.create(loanAgreement, caseId));

        verify(processEngineCommand, never()).startLoanAgreement(caseId, loanAgreement.getLoanAgreementNumber());
    }

    @Test
    void should_call_distributor_and_set_status_accepted() {
        LoanAgreementNumber loanAgreementNumber = new LoanAgreementNumber(1L);
        LoanAgreement loanAgreement = createLoanAgreementWithNumber();
        when(loanAgreementQuery.loadByNumber(loanAgreementNumber)).thenReturn(loanAgreement);

        classUnderTest.accept(loanAgreementNumber);

        verify(loanAgreementDistributor).sendLoanAgreement(loanAgreement, true);
    }

    @Test
    void should_call_distributor_and_set_status_reject() {
        LoanAgreementNumber loanAgreementNumber = new LoanAgreementNumber(1L);
        LoanAgreement loanAgreement = createLoanAgreementWithNumber();
        when(loanAgreementQuery.loadByNumber(loanAgreementNumber)).thenReturn(loanAgreement);

        classUnderTest.reject(loanAgreementNumber);

        verify(loanAgreementDistributor).sendLoanAgreement(loanAgreement, false);
    }
}