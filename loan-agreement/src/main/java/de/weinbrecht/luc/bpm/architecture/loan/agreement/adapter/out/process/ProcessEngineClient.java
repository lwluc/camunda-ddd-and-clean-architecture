package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.WorkflowCommand;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_START_EVENT_MESSAGE_REF;

@RequiredArgsConstructor
@Component
class ProcessEngineClient implements WorkflowCommand {

    private final RuntimeService runtimeService;

    @Override
    public void startLoanAgreement(CaseId caseId, LoanAgreementNumber loanAgreementNumber) {
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put(LOAN_AGREEMENT_NUMBER, loanAgreementNumber.getValue());
        runtimeService.startProcessInstanceByMessage(LOAN_START_EVENT_MESSAGE_REF, caseId.getValue(), processVariables);
    }
}
