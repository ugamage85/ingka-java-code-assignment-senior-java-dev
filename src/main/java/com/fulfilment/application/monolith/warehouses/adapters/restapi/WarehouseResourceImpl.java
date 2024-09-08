package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.exceptions.ErrorRule;
import com.fulfilment.application.monolith.exceptions.WarehouseException;
import com.fulfilment.application.monolith.mapper.WarehouseMapper;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ArchiveWarehouseUseCase;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequestScoped
@Slf4j
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject
  private WarehouseRepository warehouseRepository;
  @Inject
  private WarehouseMapper warehouseMapper;
  @Inject
  private CreateWarehouseOperation createWarehouseOperation;
  @Inject
  private ReplaceWarehouseOperation replaceWarehouseOperation;
  @Inject
  private ArchiveWarehouseUseCase archiveWarehouseUseCase;


  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  @Transactional
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    requestValidation(data);
    com.fulfilment.application.monolith.warehouses.domain.models.Warehouse model = warehouseMapper.toModel(data);
    createWarehouseOperation.create(model);
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
  @Transactional
  public void archiveAWarehouseUnitByID(String id) {
    DbWarehouse entity = warehouseRepository.findById(Long.parseLong(id));
    if (entity == null) {
      throw new WebApplicationException("Warehouse with id of " + id + " does not exist.", 404);
    }
    //warehouseRepository.remove(warehouseMapper.toModel(entity));
    archiveWarehouseUseCase.archive(warehouseMapper.toModel(entity));
  }

  @Override
  @Transactional
  public Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode, @NotNull Warehouse data) {
    com.fulfilment.application.monolith.warehouses.domain.models.Warehouse newWarehouse = warehouseMapper.toModel(data);
    newWarehouse.setBusinessUnitCode(businessUnitCode);
    replaceWarehouseOperation.replace(newWarehouse);
    return data;
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

  private void requestValidation(Warehouse data) {
    if (data.getBusinessUnitCode() == null) {
      throw new WarehouseException(ErrorRule.FIELD_IS_REQUIRED, "Business Unit Code was not set on request.");
    }
    if (data.getLocation() == null) {
      throw new WarehouseException(ErrorRule.FIELD_IS_REQUIRED, "Location was not set on request.");
    }
    if (data.getCapacity() == null) {
      throw new WarehouseException(ErrorRule.FIELD_IS_REQUIRED, "Capacity was not set on request.");
    }
    if (data.getStock() == null) {
      throw new WarehouseException(ErrorRule.FIELD_IS_REQUIRED, "Stock  was not set on request.");
    }
  }
}
