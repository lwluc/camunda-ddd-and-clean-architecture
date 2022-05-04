package de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;

public interface RecommendationPicker {
    ContentId pickContent();
}
