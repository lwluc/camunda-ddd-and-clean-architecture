package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationPicker;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.recommendation.adapter.common.ProcessConstants.CONTENT_NUMBER;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
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
        JobClient client = mock(JobClient.class, RETURNS_DEEP_STUBS);
        ActivatedJob job = mock(ActivatedJob.class);
        when(job.getKey()).thenReturn(1L);
        Map<String, Object> variables = new HashMap<>();
        variables.put(CONTENT_NUMBER, contentId.getValue());

        classUnderTest.handleJobFoo(client, job);

        verify(client.newCompleteCommand(job.getKey()).variables(variables)).send();
    }
}