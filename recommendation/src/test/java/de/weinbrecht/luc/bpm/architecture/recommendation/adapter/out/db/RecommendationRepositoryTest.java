package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db;

import de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content.ContentCRUDRepository;
import de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content.ContentEntity;
import de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content.ContentMapper;
import de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content.ContentNotFoundException;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Description;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.MailAddress;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Name;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

@MockitoSettings
class RecommendationRepositoryTest {

    @InjectMocks
    private RecommendationRepository classUnderTest;

    @Mock
    private ContentCRUDRepository contentCRUDRepository;

    @Mock(answer = CALLS_REAL_METHODS)
    private ContentMapper contentMapper;

    @Test
    void should_find_content() {
        Content content = new Content(new ContentId(1L), new Description("Test"));
        ContentEntity contentEntity = new ContentEntity();
        contentEntity.setId(content.getContentId().getValue());
        contentEntity.setContent(content.getDescription().getValue());
        when(contentCRUDRepository.findById(content.getContentId().getValue())).thenReturn(of(contentEntity));

        Content result = classUnderTest.findContentById(content.getContentId());

        assertThat(result).isEqualTo(content);
    }

    @Test
    void should_find_content_throw_custom_exception() {
        ContentId contentId = new ContentId(1L);
        when(contentCRUDRepository.findById(contentId.getValue())).thenReturn(empty());

        assertThrows(ContentNotFoundException.class,
                () -> classUnderTest.findContentById(contentId));
    }

    @Test
    void should_find_customer() {
        Customer customer = new Customer(
                new CustomerId("A-11"),
                new Name("Max Mustermann"),
                new MailAddress("max@mustermann.de")
        );

        Customer result = classUnderTest.findCustomerById(customer.getCustomerId());

        assertThat(result).isEqualTo(customer);
    }
}