package com.fulfilment.application.monolith.warehouses.domain.validator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class CreateWarehouseValidatorProducer {


    @Produces
    public List<CreateWarehouseValidator> produceCreateWarehouseValidators(ExistsByBuCodeValidator existsByBuCodeValidator,
                                                                           LocationValidator locationValidator,
                                                                           FeasibilityValidator feasibilityValidator,
                                                                           CapacityByLocationValidator capacityAndStockValidator) {
        return List.of(existsByBuCodeValidator, locationValidator, feasibilityValidator, capacityAndStockValidator);
    }
}
