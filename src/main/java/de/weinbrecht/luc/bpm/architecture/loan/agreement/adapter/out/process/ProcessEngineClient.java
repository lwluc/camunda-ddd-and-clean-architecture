package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.ProcessEngineCommand;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.common.ProcessConstants.START_EVENT_MESSAGE_REF;

@RequiredArgsConstructor
@Component
class ProcessEngineClient implements ProcessEngineCommand {

    private final RuntimeService runtimeService;

    @Override
    public void startLoanAgreement(CaseId caseId, LoanAgreementNumber loanAgreementNumber) {
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put(LOAN_AGREEMENT_NUMBER, loanAgreementNumber.getValue());
        runtimeService.startProcessInstanceByMessage(START_EVENT_MESSAGE_REF, caseId.getValue(), processVariables);
    }
}
