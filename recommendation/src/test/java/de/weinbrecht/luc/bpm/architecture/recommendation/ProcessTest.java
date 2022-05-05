package de.weinbrecht.luc.bpm.architecture.recommendation;

import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationPicker;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.SendNotification;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.filters.RecordStream;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.*;
import static io.camunda.zeebe.process.test.filters.StreamFilter.processInstance;
import static io.camunda.zeebe.protocol.record.RejectionType.NULL_VAL;
import static io.camunda.zeebe.protocol.record.intent.ProcessInstanceIntent.ELEMENT_COMPLETED;
import static io.camunda.zeebe.protocol.record.value.BpmnElementType.PROCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

// Source: https://github.com/camunda-community-hub/camunda-cloud-examples/blob/main/twitter-review-java-springboot/src/test/java/org/camunda/community/examples/twitter/TestTwitterProcess.java
@Disabled
@SpringBootTest
@ZeebeSpringTest
class ProcessTest {

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
    private RecommendationPicker recommendationPicker;

    @MockBean
    private SendNotification sendNotification;

    @Test
    void shouldExecuteProcess_happy_path() throws Exception {
        zeebe.newPublishMessageCommand().messageName(START_EVENT_MESSAGE_REF).correlationKey("").send();

//        waitForProcessInstanceCompleted(getProcessInstanceId(RecordStream.of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION), Duration.ofSeconds(10));

        waitForTaskAndComplete(PICK_CONTENT_SERVICE_TASK, PICK_CONTENT_TASK);
        waitForTaskAndComplete(SEND_RECOMMENDATION_SERVICE_TASK, SEND_RECOMMENDATION_TASK);

        assertTrue(isProcessInstanceCompleted(RecordStream.of(zeebeTestEngine.getRecordStreamSource()), PROCESS_DEFINITION));

        verify(recommendationPicker).pickContent();
        verify(sendNotification).send(any());
    }

    private ActivatedJob waitForTaskAndComplete(String taskId, String jobName) throws Exception {
        // Let the workflow engine do whatever it needs to do
        zeebeTestEngine.waitForIdleState(Duration.ofSeconds(10));

        // Now get all user tasks
        List<ActivatedJob> jobs = zeebe.newActivateJobsCommand().jobType(jobName).maxJobsToActivate(1).send().join().getJobs();

        // Should be only one
        assertTrue(jobs.size() > 0, "Job for user task '" + taskId + "' does not exist");
        ActivatedJob taskJob = jobs.get(0);
        // Make sure it is the right one
        if (taskId != null) {
            assertEquals(taskId, taskJob.getElementId());
        }

        zeebe.newCompleteCommand(taskJob.getKey()).send().join();

        return taskJob;
    }

    private ActivatedJob waitForTaskAndComplete(String taskId, String jobName, Map<String, Object> variables) throws Exception {
        ActivatedJob taskJob = this.waitForTaskAndComplete(taskId, jobName);
        zeebe.newCompleteCommand(taskJob.getKey()).variables(variables).send().join();
        return taskJob;
    }

    private boolean isProcessInstanceCompleted(RecordStream recordStream, String bpmnProcessId) {
        return processInstance(recordStream).withBpmnProcessId(bpmnProcessId).withRejectionType(NULL_VAL).withBpmnElementType(PROCESS).withIntent(ELEMENT_COMPLETED).stream().findFirst().isPresent();
    }

    private long getProcessInstanceId(RecordStream recordStream, String bpmnProcessId) {
        return processInstance(recordStream).withBpmnProcessId(bpmnProcessId).withRejectionType(NULL_VAL).withBpmnElementType(PROCESS).stream().findFirst().get().getKey();
    }
}
