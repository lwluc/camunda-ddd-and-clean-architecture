package de.weinbrecht.luc.bpm.architecture.context;

import de.weinbrecht.luc.bpm.architecture.notification.usecase.in.SendNotification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ContextTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void should_start_and_contain_domain_service() {
        assertThat(context.getBean(SendNotification.class)).isNotNull();
    }
}
