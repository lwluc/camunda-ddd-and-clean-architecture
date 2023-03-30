package de.weinbrecht.luc.bpm.architecture;

import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.junit5.ProcessEngineExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.entry;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = "element_template_test.bpmn")
class ElementTemplateTest {
    private static final String PROCESS_KEY = "P_Element_Template_Test";
    private static final String ST_SEND_NOTIFICATION = "ST_SendNotification";

    @Test
    public void should_test_element_template() {
        final String name = "Tester Tommy";
        final String address = "foo.bar@test";
        ProcessInstance processInstance = runtimeService()
                .startProcessInstanceByKey(PROCESS_KEY, Map.of(
                        "name", name,
                        "address", address
                ));
        assertThat(processInstance)
                .isStarted()
                .isWaitingAt(ST_SEND_NOTIFICATION);

        assertThat(processInstance).hasVariables("name");

        Map<String, Object> outputVariables = runtimeService().getVariables(externalTask(ST_SEND_NOTIFICATION).getExecutionId());
        Assertions.assertThat(outputVariables).contains(entry("notificationAddress", address));
        Assertions.assertThat(outputVariables).contains(entry("notificationContent", "Hello Tester Tommy,\n\nthis is a notification.\n\nMany greetings\nLuc"));

        complete(externalTask(ST_SEND_NOTIFICATION));

        assertThat(processInstance).isEnded();
    }
}
