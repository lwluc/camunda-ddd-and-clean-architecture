package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationPicker;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.CONTENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.PICK_CONTENT_TASK;

@RequiredArgsConstructor
@Component
public class PickContent {

    private final RecommendationPicker recommendationPicker;

    @ZeebeWorker(type = PICK_CONTENT_TASK)
    public void handleJobFoo(final JobClient client, final ActivatedJob job) {
        ContentId contentId = recommendationPicker.pickContent();
        Map<String, Object> variables = new HashMap<>();
        variables.put(CONTENT_NUMBER, contentId.getValue());

        client.newCompleteCommand(job.getKey())
                .variables(variables)
                .send()
                .exceptionally( throwable -> {
                    throw new CouldNotCompleteJobException(job, throwable);
                });
    }
}
