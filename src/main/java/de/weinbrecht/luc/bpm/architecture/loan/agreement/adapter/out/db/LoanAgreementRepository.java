package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.db;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreementNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementCommand;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.out.LoanAgreementQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@RequiredArgsConstructor
@Component
class LoanAgreementRepository implements LoanAgreementCommand, LoanAgreementQuery {

    private final LoanAgreementCRUDRepository crudRepository;
    private final LoanAgreementMapper mapper;

    @Override
    public LoanAgreementNumber save(LoanAgreement loanAgreement) {
        LoanAgreementEntity savedLoanAgreement = crudRepository.save(mapper.mapToDb(loanAgreement));
        return new LoanAgreementNumber(savedLoanAgreement.getId());
    }

    @Override
    public LoanAgreement loadByNumber(LoanAgreementNumber loanAgreementNumber) {
        return crudRepository.findById(loanAgreementNumber.getValue())
                .map(mapper::mapToDomain)
                .orElseThrow(() -> new LoanAgreementNotFoundException(
                        format("Could not find loan agreement to number %s", loanAgreementNumber.getValue())));
    }
}
