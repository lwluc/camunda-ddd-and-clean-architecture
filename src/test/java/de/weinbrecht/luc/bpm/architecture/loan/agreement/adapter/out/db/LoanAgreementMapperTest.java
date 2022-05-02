package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.db;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import org.junit.jupiter.api.Test;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.db.TestDataGenerator.createLoanAgreementEntity;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.TestdataGenerator.createLoanAgreement;
import static org.assertj.core.api.Assertions.assertThat;

class LoanAgreementMapperTest {

    private LoanAgreementMapper classUnderTest = new LoanAgreementMapper();

    @Test
    void should_map_all_fields_to_db() {
        LoanAgreement loanAgreement = createLoanAgreement();

        LoanAgreementEntity result = classUnderTest.mapToDb(loanAgreement);

        assertThat(result.getAmount()).isEqualTo(loanAgreement.getAmount().getValue());
        assertThat(result.getName()).isEqualTo(loanAgreement.getRecipient().getName().getValue());
        assertThat(result.getMailAddress()).isEqualTo(loanAgreement.getRecipient().getMailAddress().getValue());
        assertThat(result.getCustomerNumber()).isEqualTo(loanAgreement.getRecipient().getCustomerNumber().getValue());
    }

    @Test
    void should_map_all_fields_to_domain() {
        LoanAgreementEntity loanAgreementEntity = createLoanAgreementEntity();

        LoanAgreement result = classUnderTest.mapToDomain(loanAgreementEntity);

        assertThat(result.getLoanAgreementNumber().getValue()).isEqualTo(loanAgreementEntity.getId());
        assertThat(result.getAmount().getValue()).isEqualTo(loanAgreementEntity.getAmount());
        assertThat(result.getRecipient().getName().getValue()).isEqualTo(loanAgreementEntity.getName());
        assertThat(result.getRecipient().getMailAddress().getValue()).isEqualTo(loanAgreementEntity.getMailAddress());
        assertThat(result.getRecipient().getCustomerNumber().getValue()).isEqualTo(loanAgreementEntity.getCustomerNumber());
    }
}