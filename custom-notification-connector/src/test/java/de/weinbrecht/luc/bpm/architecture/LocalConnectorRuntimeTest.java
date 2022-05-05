package de.weinbrecht.luc.bpm.architecture;

import io.camunda.connector.runtime.ConnectorRuntimeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

public class LocalConnectorRuntimeTest {

    static class LocalConnectorRuntime {
        public static void main(String[] args) {
            SpringApplication.run(ConnectorRuntimeApplication.class, args);
        }
    }

    @Test
    public void is_connector_running_in_the_runtime()  {
        LocalConnectorRuntime.main(new String[] {});
    }
}
