package de.weinbrecht.luc.bpm.architecture.recommendation.domain.service;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationPicker;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.RecommendationQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class RecommendationService implements RecommendationPicker {

    private final RecommendationQuery recommendationQuery;

    @Override
    public ContentId pickContent() {
        // Magically decide which content is the right one for the customer
        return recommendationQuery.findContentById(new ContentId(1L)).getContentId();
    }
}
