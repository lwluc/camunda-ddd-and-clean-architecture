package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.db;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.TestdataGenerator.createLoanAgreementWithNumber;

class TestDataGenerator {
    public static LoanAgreementEntity createLoanAgreementEntity() {
        LoanAgreement loanAgreement = createLoanAgreementWithNumber();
        LoanAgreementEntity loanAgreementEntity = new LoanAgreementEntity();
        loanAgreementEntity.setId(loanAgreement.getLoanAgreementNumber().getValue());
        loanAgreementEntity.setAmount(loanAgreement.getAmount().getValue());
        loanAgreementEntity.setName(loanAgreement.getRecipient().getName().getValue());
        loanAgreementEntity.setCustomerNumber(loanAgreement.getRecipient().getCustomerNumber().getValue());
        loanAgreementEntity.setMailAddress(loanAgreement.getRecipient().getMailAddress().getValue());
        return loanAgreementEntity;
    }
}
