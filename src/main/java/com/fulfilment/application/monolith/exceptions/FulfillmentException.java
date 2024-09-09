package com.fulfilment.application.monolith.exceptions;

import lombok.Getter;

@Getter
public class FulfillmentException extends RuntimeException{
    private final ErrorRule rule;

    public FulfillmentException(ErrorRule rule) {
        super(rule.getDescription());
        this.rule = rule;
    }

    public FulfillmentException(ErrorRule rule, String message) {
        super(message);
        this.rule = rule;
    }
}
