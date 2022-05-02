package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanAgreementCRUDRepository extends CrudRepository<LoanAgreementEntity, Long> {
}
