package de.weinbrecht.luc.bpm.architecture;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.filters.RecordStream;
import io.camunda.zeebe.protocol.record.Record;
import io.camunda.zeebe.protocol.record.value.ProcessInstanceRecordValue;
import org.opentest4j.AssertionFailedError;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static io.camunda.zeebe.process.test.filters.StreamFilter.processInstance;
import static io.camunda.zeebe.protocol.record.RejectionType.NULL_VAL;
import static io.camunda.zeebe.protocol.record.intent.ProcessInstanceIntent.ELEMENT_COMPLETED;
import static io.camunda.zeebe.protocol.record.value.BpmnElementType.PROCESS;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpringProcessTestUtils {

    public static ActivatedJob waitForTaskAndComplete(
            ZeebeTestEngine zeebeTestEngine,
            ZeebeClient zeebe,
            String taskId,
            String jobType) throws Exception {
        int maxRetry = 5;

        ActivatedJob taskJob = null;
        for (int i = 0; i < maxRetry; i++) {
            taskJob = waitAndFetchJobs(zeebeTestEngine, zeebe, jobType);
            if (taskJob != null) {
                // Make sure it is the right one
                assertEquals(taskId, taskJob.getElementId());
                zeebe.newCompleteCommand(taskJob.getKey()).send().join();
            }
        }
        return taskJob;
    }

    public static ActivatedJob waitForTaskAndComplete(
            ZeebeTestEngine zeebeTestEngine,
            ZeebeClient zeebe,
            String taskId,
            String jobType,
            Map<String, Object> variables) throws Exception {
        int maxRetry = 5;

        ActivatedJob taskJob = null;
        for (int i = 0; i < maxRetry; i++) {
            taskJob = waitAndFetchJobs(zeebeTestEngine, zeebe, jobType);
            if (taskJob != null) {
                // Make sure it is the right one
                assertEquals(taskId, taskJob.getElementId());
                zeebe.newCompleteCommand(taskJob.getKey()).variables(variables).send().join();
            }
        }
        return taskJob;
    }

    public static ActivatedJob waitAndFetchJobs(
            ZeebeTestEngine zeebeTestEngine,
            ZeebeClient zeebe,
            String jobType) throws InterruptedException, TimeoutException {
        // Let the workflow engine do whatever it needs to do
        zeebeTestEngine.waitForIdleState(ofSeconds(5));

        // Now get all user tasks
        List<ActivatedJob> jobs = zeebe.newActivateJobsCommand().jobType(jobType).maxJobsToActivate(1).send().join().getJobs();

        if (jobs.isEmpty()) {
            return null;
        }
        return jobs.get(0);
    }

    public static boolean isProcessInstanceCompleted(RecordStream recordStream, String bpmnProcessId) {
        return processInstance(recordStream).withBpmnProcessId(bpmnProcessId)
                .withRejectionType(NULL_VAL)
                .withBpmnElementType(PROCESS)
                .withIntent(ELEMENT_COMPLETED)
                .stream().findFirst().isPresent();
    }

    public static Record<ProcessInstanceRecordValue> getProcessInstance(RecordStream recordStream, String bpmnProcessId) {
        return processInstance(recordStream).withBpmnProcessId(bpmnProcessId)
                .withRejectionType(NULL_VAL)
                .withBpmnElementType(PROCESS)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new AssertionFailedError("Process Instance not found", bpmnProcessId, null));
    }

    public static void hasPassedElement(String elementId, RecordStream recordStream, String bpmnProcessId) {
        processInstance(recordStream).withBpmnProcessId(bpmnProcessId)
                .withRejectionType(NULL_VAL)
                .withElementId(elementId)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new AssertionFailedError("Element not found", elementId, null));
    }

    public static long getProcessInstanceId(RecordStream recordStream, String bpmnProcessId) {
        return getProcessInstance(recordStream, bpmnProcessId).getKey();
    }
}
