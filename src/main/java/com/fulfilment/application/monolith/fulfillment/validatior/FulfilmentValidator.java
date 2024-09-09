package com.fulfilment.application.monolith.fulfillment.validatior;

import com.fulfilment.application.monolith.fulfillment.domain.model.Fulfillment;

public interface FulfilmentValidator {
    boolean validate(Fulfillment fulfillment);
}
