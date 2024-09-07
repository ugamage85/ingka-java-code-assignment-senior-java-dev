package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void create(Warehouse warehouse) {

    //todo : Revisit the error messages.
    //todo : Consider moving private methods to class

    if (warehouse == null) {
      throw new IllegalArgumentException("Warehouse cannot be null");
    }
    if (existsByBusinessUnitCode(warehouse.getBusinessUnitCode())) {
      throw new WebApplicationException("Warehouse with Business Unit Code " + warehouse.getBusinessUnitCode() + " already exists", 409);
    }
    if (!isValidLocation(warehouse.getLocation())) {
      throw new WebApplicationException("Warehouse with location " + warehouse.getLocation() + "is not valid",409);
    }
    if (!isFeasibleWarehouseCreation(warehouse.getLocation())) {
      throw new WebApplicationException("Warehouse with location " + warehouse.getLocation() + "maximum number of warehouses has already been reached",409);
    }
    if (hasExceededMaxCapacityByLocation(warehouse.getLocation(), warehouse.getCapacity())) {
      throw new WebApplicationException("Warehouse with location " + warehouse.getLocation() + "has exceeded max capacity",409);
    }
    // if all went well, create the warehouse
    warehouseStore.create(warehouse);
  }

  private boolean existsByBusinessUnitCode(String buCode) {
    try {
      // Attempt to find the warehouse; if it exists, return true
      warehouseStore.findByBusinessUnitCode(buCode);
      return true;
    } catch (WebApplicationException exception) {
      // If the warehouse is not found (404), return false
      if (exception.getResponse().getStatus() == 404) {
        return false;
      } else {
        // Rethrow other exceptions
        throw exception;
      }
    }
  }

  private boolean isValidLocation(String identifier) {
    try {
      locationResolver.resolveByIdentifier(identifier);
    } catch (WebApplicationException exception) {
      if (exception.getResponse().getStatus() == 404) {
        return false;
      } else {
        throw exception;
      }
    }
    return true;
  }

  //Check if a new warehouse can be created at the specified location or if the maximum number of warehouses has already been reached.
  private boolean isFeasibleWarehouseCreation(String identifier) {
    try {
      var location = locationResolver.resolveByIdentifier(identifier);
      var nofWarehousesInLocation = warehouseStore.getAll().stream().filter(warehouse -> warehouse.getLocation().equals(location.getIdentification())).count();
      return location.getMaxNumberOfWarehouses() > nofWarehousesInLocation;
    } catch (WebApplicationException exception) {
      if (exception.getResponse().getStatus() == 404) {
        return false;
      } else {
        throw exception;
      }
    }
  }

  //Validate the warehouse capacity, ensuring it does not exceed the maximum capacity associated with the location and that it can handle the stock informed.
  private boolean hasExceededMaxCapacityByLocation(String identifier, int warehouseCapacity) {
    try {
      var location = locationResolver.resolveByIdentifier(identifier);
      return warehouseCapacity > location.getMaxCapacity();
    } catch (WebApplicationException exception) {
      if (exception.getResponse().getStatus() == 404) {
        return false;
      } else {
        throw exception;
      }
    }
  }
}
