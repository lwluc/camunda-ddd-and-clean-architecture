package de.weinbrecht.luc.bpm.architecture.loan.agreement.domain.model;

import io.github.domainprimitives.type.ValueObject;

import static io.github.domainprimitives.validation.Constraints.isGreatThanOrEqual;


public class Amount extends ValueObject<Integer> {
    public Amount(Integer value) {
        super(value, isGreatThanOrEqual(100));
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && this.getClass() == o.getClass()) {
            ValueObject<Integer> valueObject = (ValueObject)o;
            return (valueObject.getValue()).equals(this.getValue());
        } else {
            return false;
        }
    }
}
