package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationPicker;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.CONTENT_NUMBER;
import static org.mockito.Mockito.*;

@MockitoSettings
class PickContentTest {

    @InjectMocks
    private PickContent classUnderTest;

    @Mock
    private RecommendationPicker recommendationPicker;

    @Test
    void should_call_service_and_set_variable() {
        ContentId contentId = new ContentId(1L);
        when(recommendationPicker.pickContent()).thenReturn(contentId);
        DelegateExecution delegateExecution = mock(DelegateExecution.class);

        classUnderTest.execute(delegateExecution);

        verify(delegateExecution).setVariable(CONTENT_NUMBER, contentId.getValue());
    }
}