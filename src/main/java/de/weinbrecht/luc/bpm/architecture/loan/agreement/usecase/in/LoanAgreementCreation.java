package de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;

public interface LoanAgreementCreation {
    void create(LoanAgreement loanAgreement, CaseId caseId);
}
