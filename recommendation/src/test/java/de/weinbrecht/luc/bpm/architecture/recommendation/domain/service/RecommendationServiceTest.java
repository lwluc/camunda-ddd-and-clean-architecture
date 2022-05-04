package de.weinbrecht.luc.bpm.architecture.recommendation.domain.service;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Description;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.RecommendationQuery;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.StartRecommendation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class RecommendationServiceTest {

    @InjectMocks
    private RecommendationService classUnderTest;

    @Mock
    private RecommendationQuery recommendationQuery;

    @Mock
    private StartRecommendation startRecommendation;

    @Test
    void should_magically_pick_the_content() {
        ContentId contentId = new ContentId(1L);
        Content content = new Content(contentId, new Description("Test"));
        when(recommendationQuery.findContentById(contentId)).thenReturn(content);

        ContentId result = classUnderTest.pickContent();

        assertThat(result).isEqualTo(contentId);
    }

    @Test
    void should_start_recommendation() {
        CustomerId customerId = new CustomerId("Test");
        String loanCaseId = "11";
        Long loanAgreementNumber = 1L;

        classUnderTest.create(loanCaseId, loanAgreementNumber, customerId);

        verify(startRecommendation).start(loanCaseId + "-" + loanAgreementNumber, customerId);
    }
}