package de.weinbrecht.luc.bpm.architecture;

import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Address;
import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.in.SendNotification;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendCustomNotificationTaskHandler {

    private final SendNotification sendNotification;
    private final ZeebeClient client;

    final static String ADDRESS_INPUT_NAME = "notificationAddress";
    final static String CONTENT_INPUT_NAME = "notificationContent";

    @JobWorker(type = "de.weinbrecht.luc.bpm.architecture.sendcustomnotificationtaskhandler:1", autoComplete = false)
    public void execute(ActivatedJob job) {
        log.debug("Entering Read Mail handler with variables: {}", job.getVariables());

        Address address = new Address((String) job.getVariablesAsMap().get(ADDRESS_INPUT_NAME));
        Content content = new Content((String) job.getVariablesAsMap().get(CONTENT_INPUT_NAME));

        try {
            sendNotification.pushNotification(address, content);
            log.debug("Executing job {}", job.getProcessInstanceKey());
            client.newCompleteCommand(job)
                    .send()
                    .exceptionally( throwable -> {
                        throw new RuntimeException(format("Could not complete job %s", job.getProcessInstanceKey()), throwable);
                    });
        } catch (Exception e) {
            log.error("Could not send notification to {}", address.getValue(), e);
        }
    }
}
