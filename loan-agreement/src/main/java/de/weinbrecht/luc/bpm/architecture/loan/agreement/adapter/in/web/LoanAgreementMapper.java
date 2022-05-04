package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.web;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.Amount;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CustomerNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.MailAddress;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.Name;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.Recipient;
import org.springframework.stereotype.Component;

@Component("LoanAgreementMapperWeb")
class LoanAgreementMapper {
    public LoanAgreement mapToDomain(LoanAgreementResource loanAgreementResource) {
        return new LoanAgreement(
                new Recipient(
                        new CustomerNumber(loanAgreementResource.getCustomerNumber()),
                        new Name(loanAgreementResource.getName()),
                        new MailAddress(loanAgreementResource.getMailAddress())
                ),
                new Amount(loanAgreementResource.getAmount())
        );
    }
}
