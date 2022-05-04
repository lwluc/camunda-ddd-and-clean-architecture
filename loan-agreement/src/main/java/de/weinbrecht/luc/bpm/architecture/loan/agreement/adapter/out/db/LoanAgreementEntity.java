package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.db;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
class LoanAgreementEntity {

    @Id
    @GeneratedValue(strategy= AUTO)
    private Long id;
    private Integer amount;
    private String customerNumber;
    private String name;
    private String mailAddress;
}
