package de.weinbrecht.luc.bpm.architecture.recommendation.domain.model.customer;

import io.github.domainprimitives.type.ValueObject;

import java.util.regex.Pattern;

import static io.github.domainprimitives.validation.Constraints.isPattern;

public class MailAddress extends ValueObject<String> {

    public static final String VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-zA-Z0-9_!#$%&’*+=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
                    Pattern.CASE_INSENSITIVE).pattern();


    public MailAddress(String value) {
        super(value, isPattern(VALID_EMAIL_ADDRESS_REGEX));
    }
}
