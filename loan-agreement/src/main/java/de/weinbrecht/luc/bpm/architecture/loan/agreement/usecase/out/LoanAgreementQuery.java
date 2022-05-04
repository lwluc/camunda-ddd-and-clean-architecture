package de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;

public interface LoanAgreementQuery {
    LoanAgreement loadByNumber(LoanAgreementNumber loanAgreementNumber);
}
