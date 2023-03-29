package de.weinbrecht.luc.bpm.architecture.notification.domain.model;

import io.github.domainprimitives.type.ValueObject;

import static io.github.domainprimitives.validation.Constraints.hasMinLength;

public class Content extends ValueObject<String> {
    public Content(String value) {
        super(value, "Content", hasMinLength(5));
    }
}