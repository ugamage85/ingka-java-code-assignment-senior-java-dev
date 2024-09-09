package com.fulfilment.application.monolith.fulfillment.validatior;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class CreateFulfillmentValidatorProducer {
    @Produces
    public List<FulfilmentValidator> produceFulfillmentValidators(ProductFulfillmentValidator productFulfillmentValidator,
                                                                          StoreFulfillmentValidator storeFulfillmentValidator, WarehouseFulfillmentValidator warehouseFulfillmentValidator) {
        return List.of(productFulfillmentValidator, storeFulfillmentValidator, warehouseFulfillmentValidator);
    }
}
