package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.RecommendationTrigger;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.SEND_CROSS_SELLING_RECOMMENDATION_TASK;

@RequiredArgsConstructor
@Component
public class SendCrossSellingRecommendation {

    private final RecommendationTrigger recommendationTrigger;
    private final LoanAgreementQuery loanAgreementQuery;

    @ZeebeWorker(type = SEND_CROSS_SELLING_RECOMMENDATION_TASK, fetchVariables = LOAN_AGREEMENT_NUMBER)
    public void handleJobFoo(final JobClient client, final ActivatedJob job) {
        Long loanAgreementNumber = ((Number) job.getVariablesAsMap().get(LOAN_AGREEMENT_NUMBER)).longValue();
        LoanAgreement loanAgreement = loanAgreementQuery.loadByNumber(new LoanAgreementNumber(loanAgreementNumber));

        recommendationTrigger.startLoanAgreement(new CaseId("11"), loanAgreement);

        client.newCompleteCommand(job.getKey())
                .send()
                .exceptionally(throwable -> {
                    throw new CouldNotCompleteJobException(job, throwable);
                });
    }
}
