package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.web;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationCreation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("recommendation")
public class RecommendationController {

    private final RecommendationCreation recommendationCreation;

    @PostMapping("/{caseId}")
    public void create(@RequestBody RecommendationResource recommendationResource,
                       @PathVariable String caseId) {
        recommendationCreation.create(
                caseId,
                recommendationResource.getLoanAgreementNumber(),
                new CustomerId(recommendationResource.getCustomerNumber())
        );
    }
}
