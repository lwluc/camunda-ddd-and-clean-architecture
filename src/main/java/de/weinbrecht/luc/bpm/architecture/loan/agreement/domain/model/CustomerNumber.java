package de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model;

import io.github.domainprimitives.type.ValueObject;

import static io.github.domainprimitives.validation.Constraints.isNotBlank;

public class CustomerNumber extends ValueObject<String> {
    public CustomerNumber(String value) {
        super(value, isNotBlank());
    }
}
