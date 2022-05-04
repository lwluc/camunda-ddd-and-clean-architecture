package de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;

public interface LoanAgreementDistributor {
    void sendLoanAgreement(LoanAgreement loanAgreement, boolean accepted);
}
