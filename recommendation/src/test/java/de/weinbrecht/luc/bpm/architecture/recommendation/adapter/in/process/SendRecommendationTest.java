package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Description;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Recommendation;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.MailAddress;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Name;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.RecommendationQuery;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.SendNotification;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.CONTENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.CUSTOMER_NUMBER;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;

@MockitoSettings
class SendRecommendationTest {

    @InjectMocks
    private SendRecommendation classUnderTest;

    @Mock
    private SendNotification sendNotification;

    @Mock
    RecommendationQuery recommendationQuery;

    @Test
    void should_load_data_and_call_service_to_send_notification() {
        ContentId contentId = new ContentId(1L);
        CustomerId customerNumber = new CustomerId("A1");
        JobClient client = mock(JobClient.class, RETURNS_DEEP_STUBS);
        ActivatedJob job = mock(ActivatedJob.class);
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put(CONTENT_NUMBER, contentId.getValue().intValue());
        processVariables.put(CUSTOMER_NUMBER, customerNumber.getValue());
        when(job.getVariablesAsMap()).thenReturn(processVariables);
        when(job.getKey()).thenReturn(1L);
        Content content = new Content(contentId, new Description("Foo"));
        when(recommendationQuery.findContentById(contentId)).thenReturn(content);
        Customer customer = createCustomer(customerNumber);
        when(recommendationQuery.findCustomerById(customerNumber)).thenReturn(customer);

        classUnderTest.handleJobFoo(client, job);

        verify(sendNotification).send(new Recommendation(customer, content));
        verify(client.newCompleteCommand(job.getKey())).send();
    }

    private Customer createCustomer(CustomerId customerId) {
        return new Customer(
                customerId,
                new Name("Tester"),
                new MailAddress("tester@ewb.io")
        );
    }
}