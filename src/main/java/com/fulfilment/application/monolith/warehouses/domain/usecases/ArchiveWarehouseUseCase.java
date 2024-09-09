package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArchiveWarehouseUseCase implements ArchiveWarehouseOperation {

  private final WarehouseStore warehouseStore;

  public ArchiveWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  @Override
  public void archive(Warehouse warehouse) {
    //Two things can be done here
    //  1. Set the archivedAt field to the current time
    //  2. Remove the warehouse from the database
    //Since the WarehouseResource implementation(of archiveAWarehouseUnitByID) removes the warehouse from the database(and insert into ex: archiveWarehouse table for audit purpose), I decided to do the same here
    //so, it is further support to preserve businessUnitCode uniqueness after the new warehouse is created.

    //warehouse.setArchivedAt(LocalDateTime.now());
    //warehouseStore.update(warehouse);

    if (warehouse == null) {
      throw new IllegalArgumentException("Warehouse cannot be null");
    }
    warehouseStore.remove(warehouse);
  }
}
