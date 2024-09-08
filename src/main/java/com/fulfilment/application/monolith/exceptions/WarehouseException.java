package com.fulfilment.application.monolith.exceptions;

import lombok.Getter;

@Getter
public class WarehouseException extends RuntimeException {
    private final ErrorRule rule;

    public WarehouseException(ErrorRule rule) {
        super(rule.getDescription());
        this.rule = rule;
    }

    public WarehouseException(ErrorRule rule, String message) {
        super(message);
        this.rule = rule;
    }
}
