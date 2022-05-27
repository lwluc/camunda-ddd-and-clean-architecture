package de.weinbrecht.luc.bpm.architecture.recommendation;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Description;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Recommendation;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.MailAddress;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Name;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationCreation;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationPicker;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.RecommendationQuery;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.SendNotification;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import static de.weinbrecht.luc.bpm.architecture.SpringProcessTestUtils.isProcessInstanceCompleted;
import static de.weinbrecht.luc.bpm.architecture.SpringProcessTestUtils.waitForTaskAndComplete;
import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.*;
import static io.camunda.zeebe.process.test.filters.RecordStream.of;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

// https://github.com/camunda-community-hub/camunda-8-examples/blob/main/twitter-review-java-springboot/src/test/java/org/camunda/community/examples/twitter/TestTwitterProcess.java
@SpringBootTest
@ZeebeSpringTest
@Disabled("Flaky if runs together with the non Spring Process Tests")
@MockBean(RecommendationCreation.class)
class SpringProcessTest {

    @Autowired
    private ZeebeClient zeebe;

    @Autowired
    private ZeebeTestEngine zeebeTestEngine;

    @MockBean
    private RecommendationPicker recommendationPicker;

    @MockBean
    private RecommendationQuery recommendationQuery;

    @MockBean
    private SendNotification sendNotification;

    private final ContentId contentId = new ContentId(1L);
    private final CustomerId customerId = new CustomerId("Test-11");
    private final Recommendation recommendation = new Recommendation(
            new Customer(
                    customerId,
                    new Name("Tester"),
                    new MailAddress("tester@ewb.io")
            ),
            new Content(contentId, new Description("Foo"))
    );

    public static final String PROCESS_DEFINITION = "Cross_Selling_Recommendation";

    private static final String PICK_CONTENT_SERVICE_TASK = "PickContentServiceTask";
    private static final String SEND_RECOMMENDATION_SERVICE_TASK = "SendRecommendationServiceTask";

    @BeforeEach
    void setUp() {
        when(recommendationPicker.pickContent()).thenReturn(contentId);

        when(recommendationQuery.findContentById(contentId)).thenReturn(recommendation.getContent());
        when(recommendationQuery.findCustomerById(customerId)).thenReturn(recommendation.getCustomer());
    }

    @Test
    void testRunsProcess() throws Exception {
        zeebe.newPublishMessageCommand().messageName(START_EVENT_MESSAGE_REF)
                .correlationKey("")
                .variables(singletonMap(CUSTOMER_NUMBER, customerId.getValue()))
                .send();

        waitForTaskAndComplete(zeebeTestEngine, zeebe, PICK_CONTENT_SERVICE_TASK, PICK_CONTENT_TASK, singletonMap(CONTENT_NUMBER, contentId.getValue()));

        verify(recommendationPicker).pickContent();

        waitForTaskAndComplete(zeebeTestEngine, zeebe, SEND_RECOMMENDATION_SERVICE_TASK, SEND_RECOMMENDATION_TASK);

        verify(sendNotification).send(recommendation);

        assertTrue(isProcessInstanceCompleted(of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION));
    }
}
