package de.weinbrecht.luc.bpm.architecture.recommendation;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.command.DeployResourceCommandStep1;
import io.camunda.zeebe.client.api.response.ActivateJobsResponse;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.filters.RecordStream;
import io.camunda.zeebe.protocol.record.Record;
import io.camunda.zeebe.protocol.record.value.JobRecordValue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static io.camunda.zeebe.process.test.filters.StreamFilter.jobRecords;
import static io.camunda.zeebe.protocol.record.intent.JobIntent.COMPLETED;
import static io.camunda.zeebe.protocol.record.intent.JobIntent.CREATED;
import static java.lang.String.format;
import static java.time.Duration.ofSeconds;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

// https://github.com/camunda/zeebe-process-test/blob/main/qa/abstracts/src/main/java/io/camunda/zeebe/process/test/qa/abstracts/util/Utilities.java
public class ProcessTestUtils {

    public static DeploymentEvent deployResource(final ZeebeClient client, final String resource) {
        return deployResources(client, resource);
    }

    public static DeploymentEvent deployResources(
            final ZeebeClient client, final String... resources) {
        final DeployResourceCommandStep1 commandStep1 = client.newDeployResourceCommand();

        DeployResourceCommandStep1.DeployResourceCommandStep2 commandStep2 = null;
        for (final String process : resources) {
            if (commandStep2 == null) {
                commandStep2 = commandStep1.addResourceFromClasspath(process);
            } else {
                commandStep2 = commandStep2.addResourceFromClasspath(process);
            }
        }

        return commandStep2.send().join();
    }

    public static PublishMessageResponse sendMessage(
            final ZeebeTestEngine engine,
            final ZeebeClient client,
            final String messageName,
            final String correlationKey)
            throws InterruptedException, TimeoutException {
    return sendMessage(engine, client, messageName, correlationKey, emptyMap());
    }

    public static PublishMessageResponse sendMessage(
            final ZeebeTestEngine engine,
            final ZeebeClient client,
            final String messageName,
            final String correlationKey,
            final Map<String, Object> variables)
            throws InterruptedException, TimeoutException {
        final PublishMessageResponse response =
                client
                        .newPublishMessageCommand()
                        .messageName(messageName)
                        .correlationKey(correlationKey)
                        .variables(variables)
                        .send()
                        .join();
        engine.waitForIdleState(ofSeconds(1));
        return response;
    }

    public static ActivateJobsResponse activateSingleJob(
            final ZeebeClient client, final String jobType) {
        return client.newActivateJobsCommand().jobType(jobType).maxJobsToActivate(1).send().join();
    }

    public static void completeTaskWithType(
            final ZeebeTestEngine engine, final ZeebeClient client, final String taskId, final String jobType)
            throws InterruptedException, TimeoutException {
        final List<Record<JobRecordValue>> records =
                jobRecords(RecordStream.of(engine.getRecordStreamSource()))
                        .withElementId(taskId)
                        .withIntent(CREATED)
                        .stream()
                        .collect(toList());

        jobRecords(RecordStream.of(engine.getRecordStreamSource()))
                .withElementId(taskId)
                .withIntent(COMPLETED)
                .stream()
                .forEach(record -> records.removeIf(r -> record.getKey() == r.getKey()));

        if (!records.isEmpty()) {
            final Record<JobRecordValue> lastRecord;
            lastRecord = records.get(records.size() - 1);
            assertThat(lastRecord.getValue().getType()).isEqualTo(jobType);
            client.newCompleteCommand(lastRecord.getKey()).send().join();
        } else {
            throw new IllegalStateException(
                    format("Tried to complete task `%s`, but it was not found", taskId));
        }

        engine.waitForIdleState(ofSeconds(1));
    }
}
