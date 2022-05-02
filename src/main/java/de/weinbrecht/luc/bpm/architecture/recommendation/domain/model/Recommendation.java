package de.weinbrecht.luc.bpm.architecture.recommendation.domain.model;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;
import io.github.domainprimitives.object.Aggregate;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Recommendation extends Aggregate {

    private final Customer customer;
    private final Content content;

    public Recommendation(Customer customer, Content content) {
        this.customer = customer;
        this.content = content;
        this.validate();
    }

    @Override
    protected void validate() {
        validateNotNull(customer, "Customer");
        validateNotNull(content, "Content");
        evaluateValidations();
    }
}
