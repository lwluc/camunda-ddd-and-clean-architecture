package de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.MailAddress;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.Name;
import io.github.domainprimitives.object.ComposedValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Recipient extends ComposedValueObject {

    private final CustomerNumber customerNumber;
    private final Name name;
    private final MailAddress mailAddress;

    public Recipient(CustomerNumber customerNumber, Name name, MailAddress mailAddress) {
        this.customerNumber = customerNumber;
        this.name = name;
        this.mailAddress = mailAddress;
        this.validate();
    }

    @Override
    protected void validate() {
        validateNotNull(customerNumber, "Customer Number");
        validateNotNull(name, "Name");
        validateNotNull(mailAddress, "Mail Address");
        evaluateValidations();
    }
}
