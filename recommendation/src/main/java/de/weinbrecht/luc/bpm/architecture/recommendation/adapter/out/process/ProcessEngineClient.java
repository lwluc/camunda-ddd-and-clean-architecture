package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.process;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.StartRecommendation;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.CUSTOMER_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.START_EVENT_MESSAGE_REF;
import static java.util.Collections.singletonMap;

@RequiredArgsConstructor
@Component
public class ProcessEngineClient implements StartRecommendation {

    private final RuntimeService runtimeService;

    @Override
    public void start(String caseId, CustomerId customerId) {
        runtimeService.startProcessInstanceByMessage(
                START_EVENT_MESSAGE_REF,
                caseId,
                singletonMap(CUSTOMER_NUMBER, customerId.getValue())
        );
    }
}
