package de.weinbrecht.luc.bpm.architecture;

import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Address;
import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.in.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@ExternalTaskSubscription(topicName = "sendCustomNotification")
public class SendCustomNotificationTaskHandler implements ExternalTaskHandler {

    private final SendNotification sendNotification;

    final static String ADDRESS_INPUT_NAME = "notificationAddress";
    final static String CONTENT_INPUT_NAME = "notificationContent";

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        log.debug("Entering Send Notification handler with variables: {}", externalTask.getAllVariables());

        Address address = new Address(externalTask.getVariable(ADDRESS_INPUT_NAME));
        Content content = new Content(externalTask.getVariable(CONTENT_INPUT_NAME));

        try {
            sendNotification.pushNotification(address, content);
            log.debug("Executing job {}", externalTask.getProcessInstanceId());
            externalTaskService.complete(externalTask);
        } catch (Exception e) {
            log.error("Could not send notification to {}", address.getValue(), e);
        }
    }
}
