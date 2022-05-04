package de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;

public interface RecommendationCreation {
    void create(String loanCaseId, Long loanAgreementNumber, CustomerId customerId);
}
