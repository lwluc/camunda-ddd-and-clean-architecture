package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.web;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoanAgreementMapperTest {

    private LoanAgreementMapper classUnderTest = new LoanAgreementMapper();

    @Test
    void should_map_all_fields() {
        LoanAgreementResource loanAgreementResource = createLoanAgreementResource();

        LoanAgreement result = classUnderTest.mapToDomain(loanAgreementResource);

        assertThat(result).isNotNull();
        assertThat(result.getAmount().getValue()).isEqualTo(loanAgreementResource.getAmount());
        assertThat(result.getRecipient().getName().getValue()).isEqualTo(loanAgreementResource.getName());
        assertThat(result.getRecipient().getMailAddress().getValue()).isEqualTo(loanAgreementResource.getMailAddress());
        assertThat(result.getRecipient().getCustomerNumber().getValue()).isEqualTo(loanAgreementResource.getCustomerNumber());
    }

    private LoanAgreementResource createLoanAgreementResource() {
        LoanAgreementResource loanAgreementResource = new LoanAgreementResource();
        loanAgreementResource.setAmount(400);
        loanAgreementResource.setCustomerNumber("A11");
        loanAgreementResource.setMailAddress("tester@web.io");
        loanAgreementResource.setName("Tester");
        return loanAgreementResource;
    }
}