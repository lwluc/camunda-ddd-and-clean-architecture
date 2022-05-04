package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementStatusCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApproveLoanAgreement implements JavaDelegate {

    private final LoanAgreementStatusCommand loanAgreementStatusCommand;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Long loanAgreementNumber = (Long) delegateExecution.getVariable(LOAN_AGREEMENT_NUMBER);
        loanAgreementStatusCommand.accept(new LoanAgreementNumber(loanAgreementNumber));
    }
}
