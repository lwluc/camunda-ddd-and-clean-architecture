package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.process;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementStatusCommand;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_NUMBER;
import static de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common.ProcessConstants.LOAN_AGREEMENT_TASK;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApproveLoanAgreement {

    private final LoanAgreementStatusCommand loanAgreementStatusCommand;

    @ZeebeWorker(type = LOAN_AGREEMENT_TASK, fetchVariables = LOAN_AGREEMENT_NUMBER)
    public void handleJobFoo(final JobClient client, final ActivatedJob job) {
        Long loanAgreementNumber = ((Number) job.getVariablesAsMap().get(LOAN_AGREEMENT_NUMBER)).longValue();
        loanAgreementStatusCommand.accept(new LoanAgreementNumber(loanAgreementNumber));

        client.newCompleteCommand(job.getKey())
                .send()
                .exceptionally( throwable -> {
                    throw new CouldNotCompleteJobException(job, throwable);
                });
    }
}
