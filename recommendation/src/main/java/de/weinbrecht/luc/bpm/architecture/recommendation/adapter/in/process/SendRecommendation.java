package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Recommendation;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.RecommendationQuery;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.SendNotification;
import io.camunda.zeebe.spring.client.annotation.ZeebeVariable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.*;

@RequiredArgsConstructor
@Component
public class SendRecommendation {

    private final SendNotification sendNotification;
    private final RecommendationQuery recommendationQuery;

    @ZeebeWorker(
            type = SEND_RECOMMENDATION_TASK,
            fetchVariables = {CONTENT_NUMBER, CUSTOMER_NUMBER},
            autoComplete = true
    )
    public void handleJobFoo(@ZeebeVariable Number contentNumber, @ZeebeVariable String customerNumber) {
        Content content = recommendationQuery.findContentById(new ContentId(contentNumber.longValue()));
        Customer customer = recommendationQuery.findCustomerById(new CustomerId(customerNumber));

        Recommendation recommendation = new Recommendation(customer, content);

        sendNotification.send(recommendation);
    }
}
