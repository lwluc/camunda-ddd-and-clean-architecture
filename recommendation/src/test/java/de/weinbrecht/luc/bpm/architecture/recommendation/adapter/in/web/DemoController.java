package de.weinbrecht.luc.bpm.architecture.recommendation.adapter.in.web;

import io.github.domainprimitives.validation.InvariantException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("error")
public class DemoController {

    @GetMapping()
    public void provideInvariant() {
        throw new InvariantException("Foo", "Testing");
    }
}
