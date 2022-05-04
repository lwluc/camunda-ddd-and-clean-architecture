package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.*;

@RequiredArgsConstructor
@Component
public class SendCrossSellingRecommendation implements JavaDelegate {

    private final RuntimeService runtimeService;
    private final LoanAgreementQuery loanAgreementQuery;

    @Override
    public void execute(DelegateExecution execution) {
        Long loanNumber = (Long) execution.getVariable(LOAN_AGREEMENT_NUMBER);
        LoanAgreement loanAgreement = loanAgreementQuery.loadByNumber(new LoanAgreementNumber(loanNumber));

        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put(RECOMMENDATION_CUSTOMER_NUMBER, loanAgreement.getRecipient().getCustomerNumber().getValue());

        runtimeService.startProcessInstanceByMessage(
                RECOMMENDATION_START_EVENT_MESSAGE_REF,
                buildCrossSellingBusinessKey(loanNumber, execution.getBusinessKey()),
                processVariables
        );
    }

    private String buildCrossSellingBusinessKey(Long loanNumber, String caseId) {
        return caseId + "-" + loanNumber;
    }
}
