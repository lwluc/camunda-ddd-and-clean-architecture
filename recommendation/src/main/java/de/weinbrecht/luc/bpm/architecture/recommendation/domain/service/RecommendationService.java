package de.weinbrecht.luc.bpm.architecture.recommendation.domain.service;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationCreation;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationPicker;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.RecommendationQuery;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.StartRecommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class RecommendationService implements RecommendationPicker, RecommendationCreation {

    private final RecommendationQuery recommendationQuery;
    private final StartRecommendation startRecommendation;

    @Override
    public ContentId pickContent() {
        // Magically decide which content is the right one for the customer
        return recommendationQuery.findContentById(new ContentId(1L)).getContentId();
    }

    @Override
    public void create(String loanCaseId, Long loanAgreementNumber, CustomerId customerId) {
        // Fetch customer her and save subset of needed values to recommendation
        startRecommendation.start(loanCaseId + "-" + loanAgreementNumber, customerId);
    }
}
