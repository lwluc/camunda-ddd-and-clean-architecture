package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.web;

import lombok.Data;

@Data
class LoanAgreementResource {
    private String customerNumber;
    private String name;
    private String mailAddress;
    private Integer amount;
}
