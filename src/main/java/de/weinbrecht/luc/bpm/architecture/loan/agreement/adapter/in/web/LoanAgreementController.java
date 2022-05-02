package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.web;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementCreation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("loan/agreement/")
class LoanAgreementController {

    private final LoanAgreementCreation loanAgreementCreation;
    private final LoanAgreementMapper mapper;

    @PostMapping("{caseId}")
    public void create(@RequestBody LoanAgreementResource loanAgreementResource,
                       @PathVariable String caseId) {
        loanAgreementCreation.create(mapper.mapToDomain(loanAgreementResource), new CaseId(caseId));
    }
}
