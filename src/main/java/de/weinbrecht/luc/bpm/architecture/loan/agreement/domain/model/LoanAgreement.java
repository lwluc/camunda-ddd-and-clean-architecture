package de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model;

import io.github.domainprimitives.object.Aggregate;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class LoanAgreement extends Aggregate {

    private LoanAgreementNumber loanAgreementNumber;
    private final Recipient recipient;
    private final Amount amount;

    public LoanAgreement(Recipient recipient, Amount amount) {
        this.recipient = recipient;
        this.amount = amount;
        this.validate();
    }

    public LoanAgreement(LoanAgreementNumber loanAgreementNumber, Recipient recipient, Amount amount) {
        this(recipient, amount);
        this.loanAgreementNumber = loanAgreementNumber;

        validateNotNull(loanAgreementNumber, "Loan Agreement Number");
        evaluateValidations();
    }

    protected void validate() {
        validateNotNull(recipient, "Recipient");
        validateNotNull(amount, "Loan Amount");
        evaluateValidations();
    }
}
