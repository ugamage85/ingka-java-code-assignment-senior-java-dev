package com.fulfilment.application.monolith.fulfilmentassociation.validatior;

import com.fulfilment.application.monolith.warehouses.domain.validator.CapacityByLocationValidator;
import com.fulfilment.application.monolith.warehouses.domain.validator.CreateWarehouseValidator;
import com.fulfilment.application.monolith.warehouses.domain.validator.ExistsByBuCodeValidator;
import com.fulfilment.application.monolith.warehouses.domain.validator.FeasibilityValidator;
import com.fulfilment.application.monolith.warehouses.domain.validator.LocationValidator;
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
