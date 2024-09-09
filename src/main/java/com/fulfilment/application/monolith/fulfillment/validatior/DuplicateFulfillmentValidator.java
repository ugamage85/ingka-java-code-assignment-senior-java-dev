package com.fulfilment.application.monolith.fulfillment.validatior;

import com.fulfilment.application.monolith.exceptions.ErrorRule;
import com.fulfilment.application.monolith.exceptions.FulfillmentException;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentAssociation;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentRepository;
import com.fulfilment.application.monolith.fulfillment.domain.model.Fulfillment;
import com.fulfilment.application.monolith.fulfillment.utils.FulfilmentUtil;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class DuplicateFulfillmentValidator  implements FulfilmentValidator{
    private final FulfillmentRepository fulfillmentRepository;

    @Override
    public boolean validate(Fulfillment fulfillment) {
        var uniqueWarehouseIds = FulfilmentUtil.getUniqueIds(fulfillment.getWarehouseIds());
        List<FulfillmentAssociation> existingFulfillments = fulfillmentRepository.findByStoreAndProduct(fulfillment.getStoreId(), fulfillment.getProductId());
        for (FulfillmentAssociation existingFulfillment : existingFulfillments) {
            if (existingFulfillment.getWarehouses().stream().map(DbWarehouse::getId).toList().containsAll(uniqueWarehouseIds)) {
                throw new FulfillmentException(ErrorRule.FULFILLMENT_ALREADY_EXIST);
            }
        }
        return true;
    }
}
