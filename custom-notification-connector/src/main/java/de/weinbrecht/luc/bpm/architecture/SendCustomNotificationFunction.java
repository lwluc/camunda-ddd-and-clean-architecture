package de.weinbrecht.luc.bpm.architecture;

import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Address;
import de.weinbrecht.luc.bpm.architecture.notification.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.notification.usecase.in.SendNotification;
import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@OutboundConnector(
        name = "SendNotification",
        inputVariables = {"notificationAddress", "notificationContent"},
        type = "de.weinbrecht.luc.bpm.architecture.sendcustomnotificationtaskhandler:1")
public class SendCustomNotificationFunction implements OutboundConnectorFunction {

    @Autowired
    private SendNotification sendNotification;

    @Override
    public Object execute(OutboundConnectorContext context) throws Exception {
        log.debug("Entering Read Mail handler with variables: {}", context.getVariablesAsType(NotificationInput.class));

        var input = context.getVariablesAsType(NotificationInput.class);

        Address address = new Address(input.getNotificationAddress());
        Content content = new Content(input.getNotificationContent());

        try {
            sendNotification.pushNotification(address, content);
            log.debug("Executing SendNotification job");
        } catch (Exception e) {
            log.error("Could not send notification to {}", address.getValue(), e);
        }

        return null;
    }
}
