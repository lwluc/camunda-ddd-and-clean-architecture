package de.weinbrecht.luc.bpm.architecture.recommendation.domain.model;

import io.github.domainprimitives.type.ValueObject;

import static io.github.domainprimitives.validation.Constraints.isNotBlank;

public class Description extends ValueObject<String> {
    public Description(String value) {
        super(value, isNotBlank());
    }
}
