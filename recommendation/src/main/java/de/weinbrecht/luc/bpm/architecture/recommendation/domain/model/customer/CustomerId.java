package de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer;

import io.github.domainprimitives.type.ValueObject;

import static io.github.domainprimitives.validation.Constraints.isNotBlank;

public class CustomerId extends ValueObject<String> {
    public CustomerId(String value) {
        super(value, isNotBlank());
    }
}
