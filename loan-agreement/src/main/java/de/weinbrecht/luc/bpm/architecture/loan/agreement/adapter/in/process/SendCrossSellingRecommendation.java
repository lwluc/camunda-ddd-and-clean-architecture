package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.RecommendationTrigger;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;

@RequiredArgsConstructor
@Component
public class SendCrossSellingRecommendation implements JavaDelegate {

    private final RecommendationTrigger recommendationTrigger;
    private final LoanAgreementQuery loanAgreementQuery;

    @Override
    public void execute(DelegateExecution execution) {
        Long loanNumber = (Long) execution.getVariable(LOAN_AGREEMENT_NUMBER);
        LoanAgreement loanAgreement = loanAgreementQuery.loadByNumber(new LoanAgreementNumber(loanNumber));

        recommendationTrigger.startLoanAgreement(new CaseId(execution.getBusinessKey()), loanAgreement);
    }
}
