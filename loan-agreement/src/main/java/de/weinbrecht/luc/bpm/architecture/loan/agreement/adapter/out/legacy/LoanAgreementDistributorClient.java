package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.legacy;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementDistributor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoanAgreementDistributorClient implements LoanAgreementDistributor {
    @Override
    public void sendLoanAgreement(LoanAgreement loanAgreement, boolean accepted) {
        log.info("Sending loan agreement with number {} and status [{}] to legacy system",
                loanAgreement.getLoanAgreementNumber().getValue(), accepted);
    }
}
