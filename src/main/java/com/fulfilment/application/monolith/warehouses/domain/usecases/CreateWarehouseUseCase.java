package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.domain.validator.CreateWarehouseValidator;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {
  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;
  private final List<CreateWarehouseValidator> validators;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver, List<CreateWarehouseValidator> validators) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
    this.validators = validators;
  }

  @Override
  public void create(Warehouse warehouse) {

    if (warehouse == null) {
      throw new IllegalArgumentException("Warehouse cannot be null");
    }

    boolean isValid = validators.stream().allMatch(validator -> validator.validate(warehouse));
    // if all went well, create the warehouse
    if (isValid) {
      warehouseStore.create(warehouse);
    }
  }
}
