package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.recommendation;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.RecommendationTrigger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static reactor.core.publisher.Mono.just;


@RequiredArgsConstructor
@Component
class RecommendationClient implements RecommendationTrigger {

    private final WebClient webClient;

    @Autowired
    public RecommendationClient(@Value("${custom.recommendation.address:localhost:8081}") String recommendationAddress) {
        this.webClient = WebClient.create("http://" + recommendationAddress);
    }

    @Override
    public void startLoanAgreement(CaseId caseId, LoanAgreement loanAgreement) {
        RecommendationResource recommendationResource = new RecommendationResource();
        recommendationResource.setLoanAgreementNumber(loanAgreement.getLoanAgreementNumber().getValue());
        recommendationResource.setCustomerNumber(loanAgreement.getRecipient().getCustomerNumber().getValue());

        webClient.post().uri("/recommendation/" + caseId.getValue())
                .body(just(recommendationResource), RecommendationResource.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
