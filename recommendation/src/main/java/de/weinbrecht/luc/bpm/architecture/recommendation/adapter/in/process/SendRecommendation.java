package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Recommendation;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.RecommendationQuery;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.SendNotification;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.CONTENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.CUSTOMER_NUMBER;

@RequiredArgsConstructor
@Component
public class SendRecommendation implements JavaDelegate {

    private final SendNotification sendNotification;
    private final RecommendationQuery recommendationQuery;

    @Override
    public void execute(DelegateExecution execution) {
        Long contentId = (Long) execution.getVariable(CONTENT_NUMBER);
        String customerId = (String) execution.getVariable(CUSTOMER_NUMBER);
        Content content = recommendationQuery.findContentById(new ContentId(contentId));
        Customer customer = recommendationQuery.findCustomerById(new CustomerId(customerId));

        Recommendation recommendation = new Recommendation(customer, content);

        sendNotification.send(recommendation);
    }
}
