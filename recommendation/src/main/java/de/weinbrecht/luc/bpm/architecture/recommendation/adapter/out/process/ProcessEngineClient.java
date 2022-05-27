package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.process;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.StartRecommendation;
import io.camunda.zeebe.client.ZeebeClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.CUSTOMER_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.START_EVENT_MESSAGE_REF;
import static java.util.Collections.singletonMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProcessEngineClient implements StartRecommendation {

    private final ZeebeClient client;

    @Override
    public void start(String caseId, CustomerId customerId) {
        log.info("Starting new recommendation for loan agreement with case ID {}", caseId);
        client.newPublishMessageCommand()
                .messageName(START_EVENT_MESSAGE_REF)
                .correlationKey("")
                .variables(singletonMap(CUSTOMER_NUMBER, customerId.getValue()))
                .send()
                .exceptionally(throwable -> {
                    throw new CouldNotPublishMessageException(throwable);
                });
    }
}
