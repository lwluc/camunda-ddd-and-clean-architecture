package de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CustomerNumber;
import io.github.domainprimitives.validation.InvariantException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerTest {
    @Test
    void should_create_valid_object() {
        Name name = new Name("Tester");
        MailAddress mailAddress = new MailAddress("tester@web.io");
        CustomerNumber customerNumber = new CustomerNumber("A-1");

        Customer customer = new Customer(customerNumber, name, mailAddress);

        assertThat(customer).isNotNull();
        assertThat(customer.getCustomerNumber()).isEqualTo(customerNumber);
        assertThat(customer.getName()).isEqualTo(name);
        assertThat(customer.getMailAddress()).isEqualTo(mailAddress);
    }

    @Nested
    class InvariantTest {
        @Test
        void should_throw_invariant_exception_if_customer_number_is_null() {
            Name name = new Name("Tester");
            MailAddress mailAddress = new MailAddress("tester@web.io");

            assertThrows(InvariantException.class, () -> new Customer(null, name, mailAddress));
        }

        @Test
        void should_throw_invariant_exception_if_name_is_null() {
            MailAddress mailAddress = new MailAddress("tester@web.io");
            CustomerNumber customerNumber = new CustomerNumber("A-1");

            assertThrows(InvariantException.class, () -> new Customer(customerNumber, null, mailAddress));
        }

        @Test
        void should_throw_invariant_exception_if_mail_is_null() {
            Name name = new Name("Tester");
            CustomerNumber customerNumber = new CustomerNumber("A-1");

            assertThrows(InvariantException.class, () -> new Customer(customerNumber, name, null));
        }
    }
}