package de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;

public interface StartRecommendation {
    void start(String caseId, CustomerId customerId);
}
