package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementStatusCommand;
import io.camunda.zeebe.spring.client.annotation.ZeebeVariable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_REJECTION_TASK;

@Slf4j
@RequiredArgsConstructor
@Component
public class RejectionLoanAgreement {

    private final LoanAgreementStatusCommand loanAgreementStatusCommand;

    @ZeebeWorker(type = LOAN_REJECTION_TASK, fetchVariables = LOAN_AGREEMENT_NUMBER, autoComplete = true)
    public void handleJobFoo(@ZeebeVariable Number loanAgreementNumber) {
        loanAgreementStatusCommand.reject(new LoanAgreementNumber(loanAgreementNumber.longValue()));
    }
}
