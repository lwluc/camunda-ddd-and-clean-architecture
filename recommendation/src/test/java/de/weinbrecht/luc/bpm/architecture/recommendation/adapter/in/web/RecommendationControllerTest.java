package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.web;

import de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer.CustomerId;
import de.weinbrecht.luc.bpm.architecture.recommendation.usecase.in.RecommendationCreation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@WebMvcTest(RecommendationController.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationCreation recommendationCreation;

    @Test
    void should_class_creation_on_post() throws Exception {
        String caseId = "A-11";
        Long loanAgreementNumber = 1L;
        CustomerId customerId = new CustomerId("C-8");
        String requestJson = "{\"loanAgreementNumber\": \"" + loanAgreementNumber + "\",\"customerNumber\": \"" + customerId.getValue() + "\"}";

        mockMvc.perform(
                        post("/recommendation/" + caseId)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(requestJson)
                )
                .andDo(print())
                .andExpect(status().isOk());
        verify(recommendationCreation).create(caseId, loanAgreementNumber, customerId);
    }
}