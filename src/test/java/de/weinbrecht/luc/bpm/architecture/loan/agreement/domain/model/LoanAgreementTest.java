package de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model;

import io.github.domainprimitives.validation.InvariantException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.TestdataGenerator.createRecipient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LoanAgreementTest {

    @Test
    void should_create_valid_object() {
        Recipient recipient = createRecipient();
        Amount amount = new Amount(300);

        LoanAgreement loanAgreement = new LoanAgreement(recipient, amount);

        assertThat(loanAgreement).isNotNull();
        assertThat(loanAgreement.getRecipient()).isEqualTo(recipient);
        assertThat(loanAgreement.getAmount()).isEqualTo(amount);
    }

    @Test
    void should_create_valid_object_with_numer() {
        Recipient recipient = createRecipient();
        Amount amount = new Amount(300);
        LoanAgreementNumber loanAgreementNumber = new LoanAgreementNumber(1L);

        LoanAgreement loanAgreement = new LoanAgreement(loanAgreementNumber, recipient, amount);

        assertThat(loanAgreement).isNotNull();
        assertThat(loanAgreement.getRecipient()).isEqualTo(recipient);
        assertThat(loanAgreement.getAmount()).isEqualTo(amount);
        assertThat(loanAgreement.getLoanAgreementNumber()).isEqualTo(loanAgreementNumber);
    }

    @Test
    void should_create_valid_object_with_number() {
        Recipient recipient = createRecipient();
        Amount amount = new Amount(300);
        LoanAgreementNumber loanAgreementNumber = new LoanAgreementNumber(1L);

        LoanAgreement loanAgreement = new LoanAgreement(loanAgreementNumber, recipient, amount);

        assertNotNull(loanAgreement);
        assertEquals(loanAgreement.getLoanAgreementNumber(), loanAgreementNumber);
        assertEquals(loanAgreement.getRecipient(), recipient);
        assertEquals(loanAgreement.getAmount(), amount);
    }

    @Nested
    class InvariantTest {
        @Test
        void should_throw_invariant_exception_if_recipient_is_null() {
            Amount amount = new Amount(300);
            assertThrows(InvariantException.class, () -> new LoanAgreement(null, amount));
        }

        @Test
        void should_throw_invariant_exception_if_amount_is_null() {
            assertThrows(InvariantException.class, () -> new LoanAgreement(createRecipient(), null));
        }

        @Test
        void should_throw_invariant_exception_if_number_is_null() {
            Amount amount = new Amount(300);
            assertThrows(InvariantException.class, () -> new LoanAgreement(null, createRecipient(), amount));
        }
    }
}