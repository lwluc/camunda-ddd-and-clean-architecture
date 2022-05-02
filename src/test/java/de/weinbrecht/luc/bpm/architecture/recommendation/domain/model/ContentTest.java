package de.weinbrecht.luc.bpm.architecture.recommendation.domain.model;

import io.github.domainprimitives.validation.InvariantException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContentTest {
    @Test
    void should_create_valid_object() {
        ContentId contentId = new ContentId(1L);
        Description description = new Description("Testing");

        Content content = new Content(contentId, description);

        assertThat(content).isNotNull();
        assertThat(content.getContentId()).isEqualTo(contentId);
        assertThat(content.getDescription()).isEqualTo(description);
    }

    @Nested
    class InvariantTest {
        @Test
        void should_throw_invariant_exception_if_id_is_null() {
            Description description = new Description("Testing");

            assertThrows(InvariantException.class, () -> new Content(null, description));
        }

        @Test
        void should_throw_invariant_exception_if_description_is_null() {
            ContentId contentId = new ContentId(1L);

            assertThrows(InvariantException.class, () -> new Content(contentId, null));
        }
    }
}