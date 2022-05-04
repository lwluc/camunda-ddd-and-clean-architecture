package de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;

public interface LoanAgreementStatusCommand {
    void accept(LoanAgreementNumber loanAgreementNumber);
    void reject(LoanAgreementNumber loanAgreementNumber);
}
