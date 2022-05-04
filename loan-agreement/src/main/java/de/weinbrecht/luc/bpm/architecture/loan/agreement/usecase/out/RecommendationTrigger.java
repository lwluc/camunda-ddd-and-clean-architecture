package de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;

public interface RecommendationTrigger {
    void startLoanAgreement(CaseId caseId, LoanAgreement loanAgreement);
}
