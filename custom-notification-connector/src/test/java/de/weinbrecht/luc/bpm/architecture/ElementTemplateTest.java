package de.weinbrecht.luc.bpm.architecture;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivateJobsResponse;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.extension.ZeebeProcessTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static de.weinbrecht.luc.bpm.architecture.ProcessTestUtils.*;
import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;

@ZeebeProcessTest
class ElementTemplateTest {
    private static final String PROCESS_KEY = "P_Element_Template_Test";
    private static final String ST_SEND_NOTIFICATION = "ST_SendNotification";
    private static final String JOB_TYPE = "de.weinbrecht.luc.bpm.architecture.sendcustomnotificationtaskhandler:1";
    private static final String CHECK_VARIABLES = "check_variables";

    private ZeebeTestEngine engine;
    private ZeebeClient zeebeClient;

    @BeforeEach
    void deployProcess() {
        DeploymentEvent deploymentEvent = deployResources(zeebeClient, "element_template_test.bpmn");
        assertThat(deploymentEvent).containsProcessesByResourceName("element_template_test.bpmn");
    }

    @Test
    public void should_test_element_template() throws InterruptedException, TimeoutException {
        final String name = "Tester Tommy";
        final String address = "foo.bar@test";
        ProcessInstanceEvent processInstanceEvent = zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId(PROCESS_KEY)
                .latestVersion()
                .variables(Map.of("name", name))
                .send()
                .join();
        engine.waitForIdleState(Duration.ofSeconds(1L));

        assertThat(processInstanceEvent)
                .isActive()
                .isWaitingAtElements(ST_SEND_NOTIFICATION);

        ActivateJobsResponse activatedJob = activateSingleJob(zeebeClient, JOB_TYPE);
        Map<String, Object> inputVariables = activatedJob.getJobs().get(0).getVariablesAsMap();
        Assertions.assertThat(inputVariables).containsKeys("name");

        completeTaskWithType(engine, zeebeClient, ST_SEND_NOTIFICATION, JOB_TYPE);

        ActivateJobsResponse checkVariables = activateSingleJob(zeebeClient, CHECK_VARIABLES);
        Map<String, Object> outputVariables = checkVariables.getJobs().get(0).getVariablesAsMap();
        Assertions.assertThat(outputVariables).hasSize(1);
        completeTaskWithType(engine, zeebeClient, "ST_check_Variables", CHECK_VARIABLES);

        assertThat(processInstanceEvent).isCompleted();
    }
}
