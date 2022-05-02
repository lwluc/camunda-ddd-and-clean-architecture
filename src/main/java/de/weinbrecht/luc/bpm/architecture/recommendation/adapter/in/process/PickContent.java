package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationPicker;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.CrossSellingRecommendation.CONTENT_NUMBER;

@RequiredArgsConstructor
@Component
public class PickContent implements JavaDelegate {

    private final RecommendationPicker recommendationPicker;

    @Override
    public void execute(DelegateExecution execution) {
        ContentId contentId = recommendationPicker.pickContent();
        execution.setVariable(CONTENT_NUMBER, contentId.getValue());
    }
}
