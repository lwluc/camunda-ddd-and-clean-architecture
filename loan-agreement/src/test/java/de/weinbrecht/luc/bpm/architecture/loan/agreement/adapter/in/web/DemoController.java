package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.in.web;

import de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.service.LoanAgreementException;
import io.github.domainprimitives.validation.InvariantException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("error")
public class DemoController {

    @GetMapping("/invariant")
    public void provideInvariant() {
        throw new InvariantException("Foo", "Testing");
    }

    @GetMapping("/domain")
    public void provideDomainException() {
        throw new LoanAgreementException("Bar", null);
    }
}
