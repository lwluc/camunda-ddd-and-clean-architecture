package de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.MailAddress;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.Name;

public class TestdataGenerator {

    public static Recipient createRecipient() {
        return new Recipient(new CustomerNumber("A-1"), new Name("Tester"), new MailAddress("tester@newweb.io"));
    }

    public static LoanAgreement createLoanAgreement() {
        return new LoanAgreement(
                new LoanAgreementNumber(1L),
                new Recipient(
                        new CustomerNumber("A-1"),
                        new Name("Tester"),
                        new MailAddress("tester@web.de")
                ),
                new Amount(400)
        );
    }

    public static LoanAgreement createLoanAgreementWithNumber() {
        return new LoanAgreement(
                new LoanAgreementNumber(1L),
                new Recipient(
                        new CustomerNumber("A-1"),
                        new Name("Tester"),
                        new MailAddress("tester@web.de")
                ),
                new Amount(400)
        );
    }
}
