package de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model;

import io.github.domainprimitives.type.ValueObject;

import static io.github.domainprimitives.validation.Constraints.isNotNullLong;

public class LoanAgreementNumber extends ValueObject<Long> {
    public LoanAgreementNumber(Long value) {
        super(value, isNotNullLong());
    }
}
