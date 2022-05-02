package de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CustomerNumber;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;

public interface RecommendationQuery {
    Content findContentById(ContentId contentId);
    Customer findCustomerById(CustomerNumber customerNumber);
}
