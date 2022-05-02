package de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer;

import io.github.domainprimitives.type.ValueObject;

import static io.github.domainprimitives.validation.Constraints.hasMinLength;

public class Name extends ValueObject<String> {
    public Name(String value) {
        super(value, hasMinLength(3));
    }
}
