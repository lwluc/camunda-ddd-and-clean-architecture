package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CustomerNumber;
import de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content.ContentCRUDRepository;
import de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content.ContentMapper;
import de.weinbrecht.luc.bpm.architecture.recommendation.adapter.out.db.content.ContentNotFoundException;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.Content;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.ContentId;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Customer;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.MailAddress;
import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.Name;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.out.RecommendationQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@RequiredArgsConstructor
@Component
public class RecommendationRepository implements RecommendationQuery {

    private final ContentCRUDRepository contentCRUDRepository;
    private final ContentMapper mapper;

    @Override
    public Content findContentById(ContentId contentId) {
        return contentCRUDRepository.findById(contentId.getValue())
                .map(mapper::mapToDomain)
                .orElseThrow(() -> new ContentNotFoundException(
                        format("Could not find recommendation content with id %s", contentId.getValue())));
    }

    @Override
    public Customer findCustomerById(CustomerNumber customerNumber) {
        // Here would be a own CRUDRepository for the customer information we need during recommendation
        return new Customer(
                customerNumber,
                new Name("Max Mustermann"),
                new MailAddress("max@mustermann.de")
        );
    }
}
