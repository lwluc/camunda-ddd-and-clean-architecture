package de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;

public interface WorkflowCommand {
    void startLoanAgreement(CaseId caseId, LoanAgreementNumber loanAgreementNumber);
}
