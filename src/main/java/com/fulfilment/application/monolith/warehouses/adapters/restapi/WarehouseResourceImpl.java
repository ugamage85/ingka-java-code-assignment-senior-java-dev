package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.mapper.WarehouseMapper;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject private WarehouseRepository warehouseRepository;
  @Inject private WarehouseMapper warehouseMapper;

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    com.fulfilment.application.monolith.warehouses.domain.models.Warehouse model = warehouseMapper.toModel(data);
    warehouseRepository.create(model);
    return data;
  }

  @Override
  public Warehouse getAWarehouseUnitByID(String id) {
    DbWarehouse entity = warehouseRepository.findById(Long.parseLong(id));
    if (entity == null) {
      throw new WebApplicationException("Warehouse with id of " + id + " does not exist.", 404);
    }
    return toWarehouseResponse(warehouseMapper.toModel(entity));
  }

  @Override
  public void archiveAWarehouseUnitByID(String id) {
    DbWarehouse entity = warehouseRepository.findById(Long.parseLong(id));
    if (entity == null) {
      throw new WebApplicationException("Warehouse with id of " + id + " does not exist.", 404);
    }
    warehouseRepository.remove(warehouseMapper.toModel(entity));
  }

  @Override
  public Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode, @NotNull Warehouse data) {
    com.fulfilment.application.monolith.warehouses.domain.models.Warehouse existingModel = warehouseRepository.findByBusinessUnitCode(businessUnitCode);
    warehouseRepository.update(existingModel);
    return toWarehouseResponse(existingModel);
  }

  private Warehouse toWarehouseResponse(
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
    var response = new Warehouse();
    response.setBusinessUnitCode(warehouse.getBusinessUnitCode());
    response.setLocation(warehouse.getLocation());
    response.setCapacity(warehouse.getCapacity());
    response.setStock(warehouse.getStock());

    return response;
  }
}
