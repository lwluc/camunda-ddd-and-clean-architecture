package de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer;

import io.github.domainprimitives.object.ComposedValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Customer extends ComposedValueObject {

    private final CustomerId customerId;
    private final Name name;
    private final MailAddress mailAddress;

    public Customer(CustomerId customerId, Name name, MailAddress mailAddress) {
        this.customerId = customerId;
        this.name = name;
        this.mailAddress = mailAddress;
        this.validate();
    }

    @Override
    protected void validate() {
        validateNotNull(customerId, "Customer Number");
        validateNotNull(name, "Name");
        validateNotNull(mailAddress, "Mail Address");
        evaluateValidations();
    }
}
