package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LoanAgreementCRUDRepositoryTest {

    @Autowired
    private LoanAgreementCRUDRepository classUnderTest;

    @Test
    void should_safe_entity() {
        LoanAgreementEntity result = classUnderTest.save(new LoanAgreementEntity());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
    }
}