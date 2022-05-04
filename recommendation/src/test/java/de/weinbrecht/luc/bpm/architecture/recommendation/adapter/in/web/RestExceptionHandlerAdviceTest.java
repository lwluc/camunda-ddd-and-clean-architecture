package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DemoController.class)
class RestExceptionHandlerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_handle_invariants() throws Exception {
        mockMvc.perform(get("/error")).andExpect(status().isBadRequest());
    }
}