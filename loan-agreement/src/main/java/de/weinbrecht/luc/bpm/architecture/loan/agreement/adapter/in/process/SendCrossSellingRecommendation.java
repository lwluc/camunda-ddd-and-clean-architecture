package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.RecommendationTrigger;
import io.camunda.zeebe.spring.client.annotation.ZeebeVariable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.SEND_CROSS_SELLING_RECOMMENDATION_TASK;

@RequiredArgsConstructor
@Component
public class SendCrossSellingRecommendation {

    private final RecommendationTrigger recommendationTrigger;
    private final LoanAgreementQuery loanAgreementQuery;

    @ZeebeWorker(type = SEND_CROSS_SELLING_RECOMMENDATION_TASK, autoComplete = true)
    public void handleJobFoo(@ZeebeVariable Number loanAgreementNumber, @ZeebeVariable String businessKey) {
        LoanAgreement loanAgreement = loanAgreementQuery.loadByNumber(
                new LoanAgreementNumber(loanAgreementNumber.longValue())
        );

        recommendationTrigger.startLoanAgreement(new CaseId(businessKey), loanAgreement);
    }
}
