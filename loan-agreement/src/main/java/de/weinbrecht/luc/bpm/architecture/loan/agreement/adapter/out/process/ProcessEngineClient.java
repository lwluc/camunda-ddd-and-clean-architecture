package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.WorkflowCommand;
import io.camunda.zeebe.client.ZeebeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_START_EVENT_MESSAGE_REF;

@RequiredArgsConstructor
@Component
class ProcessEngineClient implements WorkflowCommand {

    private final ZeebeClient client;

    @Override
    public void startLoanAgreement(CaseId caseId, LoanAgreementNumber loanAgreementNumber) {
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put(LOAN_AGREEMENT_NUMBER, loanAgreementNumber.getValue());
        client.newPublishMessageCommand()
                .messageName(LOAN_START_EVENT_MESSAGE_REF)
                .correlationKey("")
                .variables(processVariables)
                .send()
                .exceptionally(throwable -> {
                    throw new CouldNotPublishMessageException(throwable);
                });
    }
}
