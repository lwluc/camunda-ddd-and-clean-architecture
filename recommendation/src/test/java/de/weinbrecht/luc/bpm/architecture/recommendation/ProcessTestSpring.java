package de.weinbrecht.luc.bpm.architecture.recommendation;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Description;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Recommendation;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.MailAddress;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Name;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationPicker;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.RecommendationQuery;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.SendNotification;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.filters.RecordStream;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.*;
import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static io.camunda.zeebe.process.test.filters.RecordStream.of;
import static io.camunda.zeebe.process.test.filters.StreamFilter.processInstance;
import static io.camunda.zeebe.protocol.record.RejectionType.NULL_VAL;
import static io.camunda.zeebe.protocol.record.intent.ProcessInstanceIntent.ELEMENT_COMPLETED;
import static io.camunda.zeebe.protocol.record.value.BpmnElementType.PROCESS;
import static java.time.Duration.ofSeconds;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Source: https://github.com/camunda-community-hub/camunda-cloud-examples/blob/main/twitter-review-java-springboot/src/test/java/org/camunda/community/examples/twitter/TestTwitterProcess.java
@ActiveProfiles("test")
@SpringBootTest
@ZeebeSpringTest
class ProcessTestSpring {

    public static final String PROCESS_DEFINITION = "Cross_Selling_Recommendation";

    private static final String START_EVENT = "CrossSellingStartEvent";
    private static final String PICK_CONTENT_SERVICE_TASK = "PickContentServiceTask";
    private static final String SEND_RECOMMENDATION_SERVICE_TASK = "SendRecommendationServiceTask";
    private static final String END_EVENT = "CrossSellingRecommendationEndEvent";

    @Autowired
    private ZeebeClient zeebe;

    @Autowired
    private ZeebeTestEngine zeebeTestEngine;

    @MockBean
    private RecommendationQuery recommendationQuery;

    @MockBean
    private SendNotification sendNotification;

    @SpyBean
    private RecommendationPicker recommendationPicker;

    private final ContentId contentId = new ContentId(1L);
    private final CustomerId customerId = new CustomerId("A1");

    private final Recommendation recommendation = new Recommendation(
            new Customer(
                    customerId,
                    new Name("Tester"),
                    new MailAddress("tester@web.io")
            ),
            new Content(contentId, new Description("FooBar"))
    );

    @BeforeEach
    void setUp() {
        when(recommendationQuery.findContentById(contentId))
                .thenReturn(recommendation.getContent());
        when(recommendationQuery.findCustomerById(customerId))
                .thenReturn(recommendation.getCustomer());
    }

    @Test
    void shouldExecuteProcess_happy_path() throws Exception {
        zeebe.newPublishMessageCommand().messageName(START_EVENT_MESSAGE_REF)
                .correlationKey("")
                .variables(singletonMap(CUSTOMER_NUMBER, customerId.getValue()))
                .send();

        waitForTaskAndComplete(PICK_CONTENT_SERVICE_TASK, PICK_CONTENT_TASK, singletonMap(CONTENT_NUMBER, contentId.getValue()));
        waitForTaskAndComplete(SEND_RECOMMENDATION_SERVICE_TASK, SEND_RECOMMENDATION_TASK);

        assertTrue(isProcessInstanceCompleted(of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION));

        verify(recommendationPicker).pickContent();
        verify(sendNotification).send(recommendation);
    }

    private ActivatedJob waitForTaskAndComplete(String taskId, String jobType) throws Exception {
        int maxRetry = 5;

        ActivatedJob taskJob = null;
        for (int i = 0; i < maxRetry; i++) {
            taskJob = waitAndFetchJobs(taskId, jobType);
            if (taskJob != null) {
                // Make sure it is the right one
                assertEquals(taskId, taskJob.getElementId());
                zeebe.newCompleteCommand(taskJob.getKey()).send().join();
            }
        }
        return taskJob;
    }

    private ActivatedJob waitForTaskAndComplete(String taskId, String jobType, Map<String, Object> variables) throws Exception {
        int maxRetry = 5;

        ActivatedJob taskJob = null;
        for (int i = 0; i < maxRetry; i++) {
            taskJob = waitAndFetchJobs(taskId, jobType);
            if (taskJob != null) {
                // Make sure it is the right one
                assertEquals(taskId, taskJob.getElementId());
                zeebe.newCompleteCommand(taskJob.getKey()).variables(variables).send().join();
            }
        }
        return taskJob;
    }

    private ActivatedJob waitAndFetchJobs(String taskId, String jobType) throws InterruptedException, TimeoutException {
        // Let the workflow engine do whatever it needs to do
        zeebeTestEngine.waitForIdleState(ofSeconds(5));

        // Now get all user tasks
        List<ActivatedJob> jobs = zeebe.newActivateJobsCommand().jobType(jobType).maxJobsToActivate(1).send().join().getJobs();

        if (jobs.size() == 0) {
            return null;
        }
        return jobs.get(0);
    }

    private boolean isProcessInstanceCompleted(RecordStream recordStream, String bpmnProcessId) {
        return processInstance(recordStream).withBpmnProcessId(bpmnProcessId)
                .withRejectionType(NULL_VAL)
                .withBpmnElementType(PROCESS)
                .withIntent(ELEMENT_COMPLETED)
                .stream().findFirst().isPresent();
    }

    private long getProcessInstanceId(RecordStream recordStream, String bpmnProcessId) {
        return processInstance(recordStream).withBpmnProcessId(bpmnProcessId)
                .withRejectionType(NULL_VAL)
                .withBpmnElementType(PROCESS)
                .stream().findFirst().get().getKey();
    }
}
