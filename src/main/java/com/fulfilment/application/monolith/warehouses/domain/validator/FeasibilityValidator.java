package com.fulfilment.application.monolith.warehouses.domain.validator;

import com.fulfilment.application.monolith.exceptions.WarehouseException;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import lombok.RequiredArgsConstructor;

import static com.fulfilment.application.monolith.exceptions.ErrorRule.MAX_WAREHOUSE_NUMBER_REACHED;

@RequiredArgsConstructor
@ApplicationScoped
public class FeasibilityValidator  implements CreateWarehouseValidator {
    private final LocationResolver locationResolver;
    private final WarehouseStore warehouseStore;

    @Override
    public boolean validate(Warehouse warehouse) {
        var location = locationResolver.resolveByIdentifier(warehouse.getLocation());
        var nofWarehousesInLocation = warehouseStore.getAll().stream().filter(item -> item.getLocation().equals(location.getIdentification())).count();
        if (location.getMaxNumberOfWarehouses() < nofWarehousesInLocation) {
            throw new WarehouseException(MAX_WAREHOUSE_NUMBER_REACHED);
        }
        return true;
    }
}
