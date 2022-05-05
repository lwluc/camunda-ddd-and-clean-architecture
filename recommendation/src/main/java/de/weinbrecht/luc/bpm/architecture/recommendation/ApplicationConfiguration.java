package de.weinbrecht.luc.bpm.architecture.recommendation;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableZeebeClient
@ZeebeDeployment(resources = {"classpath:*.bpmn", "classpath:*.dmn"})
public class ApplicationConfiguration {
}
