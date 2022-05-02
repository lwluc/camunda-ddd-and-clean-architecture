package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.db;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.db.TestDataGenerator.createLoanAgreementEntity;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.TestdataGenerator.createLoanAgreement;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.TestdataGenerator.createLoanAgreementWithNumber;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

@MockitoSettings
class LoanAgreementRepositoryTest {

    @InjectMocks
    private LoanAgreementRepository classUnderTest;

    @Mock
    private LoanAgreementCRUDRepository crudRepository;

    @Mock(answer = CALLS_REAL_METHODS)
    private LoanAgreementMapper mapper;

    @Test
    void should_call_crud_repo_and_safe() {
        LoanAgreement loanAgreement = createLoanAgreement();
        LoanAgreementEntity dbEntity = createLoanAgreementEntity();
        when(crudRepository.save(mapper.mapToDb(loanAgreement))).thenReturn(dbEntity);

        LoanAgreementNumber result = classUnderTest.save(loanAgreement);

        assertThat(result.getValue()).isEqualTo(dbEntity.getId());
    }

    @Test
    void should_call_crud_repo_and_find_by_id() {
        LoanAgreement loanAgreement = createLoanAgreementWithNumber();
        when(crudRepository.findById(loanAgreement.getLoanAgreementNumber().getValue()))
                .thenReturn(of(createLoanAgreementEntity()));

        LoanAgreement result = classUnderTest.loadByNumber(loanAgreement.getLoanAgreementNumber());

        assertThat(result).isEqualTo(loanAgreement);
    }

    @Test
    void should_call_crud_repo_and_find_by_id_throw_custom_exception_if_not_found() {
        LoanAgreement loanAgreement = createLoanAgreementWithNumber();
        when(crudRepository.findById(loanAgreement.getLoanAgreementNumber().getValue())).thenReturn(empty());

        assertThrows(LoanAgreementNotFoundException.class,
                () -> classUnderTest.loadByNumber(loanAgreement.getLoanAgreementNumber()));

    }
}