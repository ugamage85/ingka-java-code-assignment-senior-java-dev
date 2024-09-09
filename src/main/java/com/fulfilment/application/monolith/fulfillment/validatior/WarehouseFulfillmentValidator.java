package com.fulfilment.application.monolith.fulfillment.validatior;

import com.fulfilment.application.monolith.exceptions.ErrorRule;
import com.fulfilment.application.monolith.exceptions.FulfillmentException;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentRepository;
import com.fulfilment.application.monolith.fulfillment.domain.model.Fulfillment;
import com.fulfilment.application.monolith.fulfillment.utils.FulfilmentUtil;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class WarehouseFulfillmentValidator  implements FulfilmentValidator{
    private final FulfillmentRepository fulfillmentRepository;
    private final  WarehouseRepository warehouseRepository;

    @Override
    public boolean validate(Fulfillment fulfillment) {
        var uniqueWarehouseIds = FulfilmentUtil.getUniqueIds(fulfillment.getWarehouseIds());
        for (Long warehouseId : uniqueWarehouseIds) {
            DbWarehouse warehouse = warehouseRepository.findById(warehouseId);
            if (warehouse != null && fulfillmentRepository.countProductsInWarehouse(warehouseId) >= 5) {
                throw new FulfillmentException(ErrorRule.WAREHOUSE_MAX_PRODUCT_TYPES_EXCEEDED);
            }
        }
        return true;
    }
}
