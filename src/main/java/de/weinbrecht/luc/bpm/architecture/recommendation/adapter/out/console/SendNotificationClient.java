package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.console;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Recommendation;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.SendNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendNotificationClient implements SendNotification {
    @Override
    public void send(Recommendation recommendation) {
        log.info("Sending recommendation '{}' to customer {}",
                recommendation.getContent().getDescription().getValue(), recommendation.getCustomer().getName());
    }
}
