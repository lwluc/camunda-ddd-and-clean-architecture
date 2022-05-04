package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.web;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.Amount;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CustomerNumber;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.MailAddress;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.Name;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.recipient.Recipient;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.usecase.in.LoanAgreementCreation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@WebMvcTest(LoanAgreementController.class)
@Import(LoanAgreementMapper.class)
class LoanAgreementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanAgreementCreation loanAgreementCreation;

    @Test
    void should_class_creation_on_post() throws Exception {
        String requestJson = "{\"customerNumber\": \"A1\",\"name\": \"Tester\",\"mailAddress\": \"tester@web.io\",\"amount\": 1100}";

        mockMvc.perform(
                post("/loan/agreement/1")
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestJson)
        )
                .andDo(print())
                .andExpect(status().isOk());
        verify(loanAgreementCreation).create(
                new LoanAgreement(
                        new Recipient(
                                new CustomerNumber("A1"),
                                new Name("Tester"),
                                new MailAddress("tester@web.io")
                        ),
                        new Amount(1100)
                ),
                new CaseId("1"));
    }
}