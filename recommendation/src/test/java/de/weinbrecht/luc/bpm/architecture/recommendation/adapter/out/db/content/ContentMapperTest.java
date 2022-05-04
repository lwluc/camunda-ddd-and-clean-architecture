package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Description;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContentMapperTest {

    private ContentMapper classUnderTest = new ContentMapper();

    @Test
    void should_map_all_fields() {
        Content content = new Content(new ContentId(1L), new Description("Test"));
        ContentEntity contentEntity = new ContentEntity();
        contentEntity.setId(content.getContentId().getValue());
        contentEntity.setContent(content.getDescription().getValue());

        Content result = classUnderTest.mapToDomain(contentEntity);

        assertThat(result).isEqualTo(content);
    }
}