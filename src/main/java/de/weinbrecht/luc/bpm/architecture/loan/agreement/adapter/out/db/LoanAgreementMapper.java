package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.db;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.Amount;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CustomerNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.MailAddress;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.Name;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.Recipient;
import org.springframework.stereotype.Component;

@Component("LoanAgreementMapperDb")
class LoanAgreementMapper {
    public LoanAgreementEntity mapToDb(LoanAgreement loanAgreement) {
        LoanAgreementEntity loanAgreementEntity = new LoanAgreementEntity();
        loanAgreementEntity.setAmount(loanAgreement.getAmount().getValue());
        Recipient recipient = loanAgreement.getRecipient();
        loanAgreementEntity.setCustomerNumber(recipient.getCustomerNumber().getValue());
        loanAgreementEntity.setName(recipient.getName().getValue());
        loanAgreementEntity.setMailAddress(recipient.getMailAddress().getValue());
        return loanAgreementEntity;
    }

    public LoanAgreement mapToDomain(LoanAgreementEntity loanAgreementEntity) {
        return new LoanAgreement(
                new LoanAgreementNumber(loanAgreementEntity.getId()),
                new Recipient(
                        new CustomerNumber(loanAgreementEntity.getCustomerNumber()),
                        new Name(loanAgreementEntity.getName()),
                        new MailAddress(loanAgreementEntity.getMailAddress())
                ),
                new Amount(loanAgreementEntity.getAmount())
        );
    }
}
