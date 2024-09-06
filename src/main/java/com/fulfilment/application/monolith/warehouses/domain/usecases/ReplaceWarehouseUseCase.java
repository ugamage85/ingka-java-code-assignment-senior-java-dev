package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;

import java.time.LocalDateTime;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final ArchiveWarehouseOperation archiveWarehouseOperation;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore, ArchiveWarehouseOperation archiveWarehouseOperation) {
    this.warehouseStore = warehouseStore;
    this.archiveWarehouseOperation = archiveWarehouseOperation;
  }

  /**
   * <p>
   * Replaces the current active Warehouse identified by
   * <code>businessUnitCode</code> unit by a new Warehouse provided in the request
   * body A Warehouse can be replaced by another Warehouse with the same Business
   * Unit Code. That means that the previous Warehouse will be archived and the
   * new Warehouse will be created assuming its place.
   * </p>
   *
   */

  @Override
  public void replace(Warehouse newWarehouse) {
    Warehouse existingWarehouse = warehouseStore.findByBusinessUnitCode(newWarehouse.getBusinessUnitCode());

    // Check if the existing warehouse exists and is not archived
    if (existingWarehouse == null || existingWarehouse.getArchivedAt() != null) {
      throw new WebApplicationException("Warehouse with Business Unit Code " + newWarehouse.getBusinessUnitCode() + " not found or is archived", 404);
    }

    // Validate if the new warehouse's capacity can accommodate the stock from the warehouse being replaced
    if (newWarehouse.getCapacity() < existingWarehouse.getStock()) {
      throw new WebApplicationException("New warehouse's capacity cannot accommodate the stock from the warehouse being replaced", 400);
    }

    // Confirm that the stock of the new warehouse matches the stock of the previous warehouse
    if (!newWarehouse.getStock().equals(existingWarehouse.getStock())) {
      throw new WebApplicationException("Stock of the new warehouse does not match the stock of the previous warehouse", 400);
    }

    //archive the existing warehouse.
    archiveWarehouseOperation.archive(existingWarehouse);

    // If all checks pass, replace the existing warehouse with the new one
    newWarehouse.setCreatedAt(existingWarehouse.getCreatedAt());
    newWarehouse.setArchivedAt(null);
    warehouseStore.create(newWarehouse);

  }
}
