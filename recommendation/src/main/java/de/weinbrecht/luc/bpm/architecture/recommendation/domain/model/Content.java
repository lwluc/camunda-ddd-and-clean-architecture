package de.weinbrecht.luc.bpm.architecture.recommendation.domain.model;

import io.github.domainprimitives.object.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Content extends Entity {

    private final ContentId contentId;
    private final Description description;

    public Content(ContentId contentId, Description description) {
        this.contentId = contentId;
        this.description = description;
        validate();
    }

    @Override
    protected void validate() {
        validateNotNull(contentId, "ContentId");
        validateNotNull(description, "Description");
        evaluateValidations();
    }
}
