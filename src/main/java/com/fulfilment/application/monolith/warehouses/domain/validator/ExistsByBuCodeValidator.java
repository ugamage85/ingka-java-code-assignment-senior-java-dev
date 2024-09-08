package com.fulfilment.application.monolith.warehouses.domain.validator;

import com.fulfilment.application.monolith.exceptions.WarehouseException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import lombok.RequiredArgsConstructor;

import static com.fulfilment.application.monolith.exceptions.ErrorRule.WAREHOUSE_ALREADY_ARCHIVED;
import static com.fulfilment.application.monolith.exceptions.ErrorRule.WAREHOUSE_ALREADY_EXIST;
import static com.fulfilment.application.monolith.exceptions.ErrorRule.WAREHOUSE_NOT_FOUND;

@ApplicationScoped
@RequiredArgsConstructor
public class ExistsByBuCodeValidator implements CreateWarehouseValidator{
    private final WarehouseStore warehouseStore;

    @Override
    public boolean validate(Warehouse warehouse) {
        try {
            Warehouse record = warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode());
            if (record != null) {
                throw new WebApplicationException("Warehouse with Business Unit Code " + warehouse.getBusinessUnitCode() + " already exists", 409);
            }
        } catch (WarehouseException exception) {
            if (exception.getRule().equals(WAREHOUSE_NOT_FOUND)) {
                return true;
            }else {
                throw exception;
            }
        }
        return false;
    }
}
