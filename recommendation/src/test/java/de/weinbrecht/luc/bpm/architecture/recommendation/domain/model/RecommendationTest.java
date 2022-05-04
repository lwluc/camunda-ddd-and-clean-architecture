package de.weinbrecht.luc.bpm.architecture.recommendation.domain.model;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.MailAddress;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Name;
import io.github.domainprimitives.validation.InvariantException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecommendationTest {
    @Test
    void should_create_valid_object() {
        Content content = new Content(new ContentId(1L), new Description("Test"));
        Customer customer = new Customer(new CustomerId("A"), new Name("Tester"), new MailAddress("tester@web.io"));

        Recommendation recommendation = new Recommendation(customer, content);

        assertThat(recommendation).isNotNull();
        assertThat(recommendation.getContent()).isEqualTo(content);
        assertThat(recommendation.getCustomer()).isEqualTo(customer);
    }

    @Nested
    class InvariantTest {
        @Test
        void should_throw_invariant_exception_if_customer_is_null() {
            Content content = new Content(new ContentId(1L), new Description("Test"));

            assertThrows(InvariantException.class, () -> new Recommendation(null, content));
        }

        @Test
        void should_throw_invariant_exception_if_content_is_null() {
            Customer customer = new Customer(new CustomerId("A"), new Name("Tester"), new MailAddress("tester@web.io"));

            assertThrows(InvariantException.class, () -> new Recommendation(customer, null));
        }
    }
}