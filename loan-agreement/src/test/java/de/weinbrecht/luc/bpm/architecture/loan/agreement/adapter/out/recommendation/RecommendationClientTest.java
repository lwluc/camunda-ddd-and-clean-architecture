package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.recommendation;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.CaseId;
import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.LoanAgreement;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model.TestdataGenerator.createLoanAgreement;
import static org.assertj.core.api.Assertions.assertThat;

class RecommendationClientTest {

    private RecommendationClient classUnderTest;

    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("localhost:%s", mockBackEnd.getPort());
        classUnderTest = new RecommendationClient(baseUrl);
    }

    @Test
    void should_class_runtime_service_to_start() throws Exception {
        CaseId caseId = new CaseId("11");
        LoanAgreement loanAgreement = createLoanAgreement();
        RecommendationResource recommendationResource = new RecommendationResource();
        recommendationResource.setLoanAgreementNumber(loanAgreement.getLoanAgreementNumber().getValue());
        recommendationResource.setCustomerNumber(loanAgreement.getRecipient().getCustomerNumber().getValue());
        String requestJson = "{\"loanAgreementNumber\":" + loanAgreement.getLoanAgreementNumber().getValue() +
                ",\"customerNumber\":\"" + loanAgreement.getRecipient().getCustomerNumber().getValue() + "\"}";

        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json"));

        classUnderTest.startLoanAgreement(caseId, loanAgreement);

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("POST");
        assertThat(recordedRequest.getPath()).isEqualTo("/recommendation/" + caseId);
        assertThat(recordedRequest.getBody().readUtf8()).isEqualTo(requestJson);
    }
}