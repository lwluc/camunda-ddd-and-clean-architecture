package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CustomerNumber;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Description;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Recommendation;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.MailAddress;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Name;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.RecommendationQuery;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.SendNotification;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.CrossSellingRecommendation.CONTENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.CrossSellingRecommendation.CUSTOMER_NUMBER;
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
        CustomerNumber customerNumber = new CustomerNumber("A1");
        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        when(delegateExecution.getVariable(CONTENT_NUMBER)).thenReturn(contentId.getValue());
        when(delegateExecution.getVariable(CUSTOMER_NUMBER)).thenReturn(customerNumber.getValue());
        Content content = new Content(contentId, new Description("Foo"));
        when(recommendationQuery.findContentById(contentId)).thenReturn(content);
        Customer customer = createCustomer(customerNumber);
        when(recommendationQuery.findCustomerById(customerNumber)).thenReturn(customer);

        classUnderTest.execute(delegateExecution);

        verify(sendNotification).send(new Recommendation(customer, content));
    }

    private Customer createCustomer(CustomerNumber customerNumber) {
        return new Customer(
                customerNumber,
                new Name("Tester"),
                new MailAddress("tester@ewb.io")
        );
    }
}