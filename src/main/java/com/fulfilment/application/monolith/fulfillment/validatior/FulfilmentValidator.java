package com.fulfilment.application.monolith.fulfillment.validatior;

import com.fulfilment.application.monolith.fulfillment.domain.Fulfillment;

public interface FulfilmentValidator {
    boolean validate(Fulfillment fulfillment);
}
