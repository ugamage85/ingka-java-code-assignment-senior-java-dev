package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.mapper.WarehouseMapper;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {
  @Inject private WarehouseMapper warehouseMapper;
  @Override
  public List<Warehouse> getAll() {
    return this.listAll().stream().map(entity -> warehouseMapper.toModel(entity)).toList();
  }

  @Override
  public void create(Warehouse warehouse) {
    var warehouseEntity = warehouseMapper.toEntity(warehouse);
    warehouseEntity.setCreatedAt(LocalDateTime.now());
    warehouseEntity.setArchivedAt(null);
    this.persist(warehouseEntity);
  }

  @Override
  public void update(Warehouse warehouse) {
    DbWarehouse entity = find("businessUnitCode", warehouse.getBusinessUnitCode()).firstResult();
    if (entity == null){
      throw new WebApplicationException("Warehouse with Business Unit Code " + warehouse.getBusinessUnitCode() + " not found", 404);
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
    if (entity == null){
      throw new WebApplicationException("Warehouse with Business Unit Code " + warehouse.getBusinessUnitCode() + " not found", 404);
    }
    this.delete(entity);
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    //DbWarehouse entity = find("businessUnitCode", buCode).firstResult();
    DbWarehouse entity = find("businessUnitCode = ?1 and archivedAt is null", buCode).firstResult();
    if (entity == null){
      throw new WebApplicationException("Warehouse with Business Unit Code " + buCode + " not found", 404);
    }
    return warehouseMapper.toModel(entity);
  }
}
