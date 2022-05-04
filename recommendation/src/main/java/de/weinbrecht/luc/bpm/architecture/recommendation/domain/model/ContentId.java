package de.weinbrecht.luc.bpm.architecture.recommendation.domain.model;

import io.github.domainprimitives.type.ValueObject;

import static io.github.domainprimitives.validation.Constraints.isNotNullLong;

public class ContentId extends ValueObject<Long> {
    public ContentId(Long value) {
        super(value, isNotNullLong());
    }
}
