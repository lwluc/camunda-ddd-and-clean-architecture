package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Recommendation;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.RecommendationQuery;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.SendNotification;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.*;

@RequiredArgsConstructor
@Component
public class SendRecommendation {

    private final SendNotification sendNotification;
    private final RecommendationQuery recommendationQuery;

    @ZeebeWorker(type = SEND_RECOMMENDATION_TASK, fetchVariables = {CONTENT_NUMBER, CUSTOMER_NUMBER})
    public void handleJobFoo(final JobClient client, final ActivatedJob job) {
        Long contentId = ((Number) job.getVariablesAsMap().get(CONTENT_NUMBER)).longValue();
        String customerId = (String) job.getVariablesAsMap().get(CUSTOMER_NUMBER);
        Content content = recommendationQuery.findContentById(new ContentId(contentId));
        Customer customer = recommendationQuery.findCustomerById(new CustomerId(customerId));

        Recommendation recommendation = new Recommendation(customer, content);

        sendNotification.send(recommendation);

        client.newCompleteCommand(job.getKey())
                .send()
                .exceptionally( throwable -> {
                    throw new CouldNotCompleteJobException(job, throwable);
                });
    }
}
