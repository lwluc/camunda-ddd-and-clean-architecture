package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.web;

import lombok.Data;

@Data
public class RecommendationResource {
    private Long loanAgreementNumber;
    private String customerNumber;
}
