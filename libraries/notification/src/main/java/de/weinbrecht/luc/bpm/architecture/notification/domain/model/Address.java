package de.weinbrecht.luc.bpm.architecture.notification.domain.model;

import io.github.domainprimitives.type.ValueObject;

import static io.github.domainprimitives.validation.Constraints.isNotBlank;

// TODO: Use more specific object like e.g. postal address or mail address
public class Address extends ValueObject<String> {
    public Address(String value) {
        super(value, "Address", isNotBlank());
    }
}
