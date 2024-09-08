package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.exceptions.ErrorRule;
import com.fulfilment.application.monolith.exceptions.WarehouseException;
import com.fulfilment.application.monolith.mapper.WarehouseMapper;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {
  @Inject
  private WarehouseMapper warehouseMapper;

  @Override
  public List<Warehouse> getAll() {
    return this.listAll().stream().map(entity -> warehouseMapper.toModel(entity)).toList();
  }

  @Override
  public void create(Warehouse warehouse) {
    var warehouseEntity = warehouseMapper.toEntity(warehouse);
    this.persist(warehouseEntity);
  }

  @Override
  public void update(Warehouse warehouse) {
    DbWarehouse entity = find("businessUnitCode", warehouse.getBusinessUnitCode()).firstResult();
    if (entity == null) {
      throw new WarehouseException(ErrorRule.WAREHOUSE_NOT_FOUND, "Warehouse with the provided businessUnitCode not found");
    }
    //todo: updatedAt is not presence. check later
    entity.setLocation(warehouse.getLocation());
    entity.setCapacity(warehouse.getCapacity());
    entity.setStock(warehouse.getStock());
    this.persist(entity);
  }

  @Override
  public void remove(Warehouse warehouse) {
    DbWarehouse entity = find("businessUnitCode", warehouse.getBusinessUnitCode()).firstResult();
    if (entity == null) {
      throw new WarehouseException(ErrorRule.WAREHOUSE_NOT_FOUND, "Warehouse with the provided businessUnitCode not found");
    }
    this.delete(entity);
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    //DbWarehouse entity = find("businessUnitCode", buCode).firstResult();
    DbWarehouse entity = find("businessUnitCode = ?1 and archivedAt is null", buCode).firstResult();
    if (entity == null) {
      throw new WarehouseException(ErrorRule.WAREHOUSE_NOT_FOUND, "Warehouse with the provided businessUnitCode not found");
    }
    return warehouseMapper.toModel(entity);
  }

  @Override
  public DbWarehouse findById(Long id) {
    DbWarehouse entity = find("id", id).firstResult();
    if (entity == null) {
      throw new WarehouseException(ErrorRule.WAREHOUSE_NOT_FOUND, "Warehouse with the provided id not found");
    }
    return entity;
  }
}
