package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.db;

class LoanAgreementNotFoundException extends RuntimeException {
    public LoanAgreementNotFoundException(String message) {
        super(message);
    }
}
