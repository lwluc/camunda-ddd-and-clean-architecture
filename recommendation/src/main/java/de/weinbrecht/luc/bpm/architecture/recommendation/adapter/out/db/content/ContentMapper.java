package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Description;
import org.springframework.stereotype.Component;

@Component
public class ContentMapper {

    public Content mapToDomain(ContentEntity contentEntity) {
        return new Content(
                new ContentId(contentEntity.getId()),
                new Description(contentEntity.getContent())
        );
    }
}
