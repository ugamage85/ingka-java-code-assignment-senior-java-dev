package com.fulfilment.application.monolith.fulfilmentassociation.validatior;

import com.fulfilment.application.monolith.fulfilmentassociation.domain.Fulfillment;

public interface FulfilmentValidator {
    boolean validate(Fulfillment fulfillment);
}
