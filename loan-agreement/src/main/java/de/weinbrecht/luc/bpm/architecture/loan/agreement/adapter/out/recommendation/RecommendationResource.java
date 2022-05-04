package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.recommendation;

import lombok.Data;

@Data
class RecommendationResource {
    private Long loanAgreementNumber;
    private String customerNumber;
}
