package de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.service;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementCreation;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementStatusCommand;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementCommand;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementDistributor;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.RecommendationTrigger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanAgreementService implements LoanAgreementCreation, LoanAgreementStatusCommand {

    private final LoanAgreementCommand loanAgreementCommand;
    private final RecommendationTrigger recommendationTrigger;
    private final LoanAgreementDistributor loanAgreementDistributor;
    private final LoanAgreementQuery loanAgreementQuery;

    @Override
    public void create(LoanAgreement loanAgreement, CaseId caseId) {
        try {
            LoanAgreement savedLoanAgreement = loanAgreementCommand.save(loanAgreement);
            recommendationTrigger.startLoanAgreement(caseId, savedLoanAgreement);
        } catch (Exception e) {
            throw new LoanAgreementException("Cloud not save the loan agreement", e);
        }
    }

    @Override
    public void accept(LoanAgreementNumber loanAgreementNumber) {
        loanAgreementDistributor.sendLoanAgreement(loanAgreementQuery.loadByNumber(loanAgreementNumber), true);
    }

    @Override
    public void reject(LoanAgreementNumber loanAgreementNumber) {
        loanAgreementDistributor.sendLoanAgreement(loanAgreementQuery.loadByNumber(loanAgreementNumber), false);
    }
}
