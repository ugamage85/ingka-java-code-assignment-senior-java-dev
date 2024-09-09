package com.fulfilment.application.monolith.fulfillment.validatior;

import com.fulfilment.application.monolith.exceptions.ErrorRule;
import com.fulfilment.application.monolith.exceptions.FulfillmentException;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentAssociation;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentRepository;
import com.fulfilment.application.monolith.fulfillment.domain.model.Fulfillment;
import com.fulfilment.application.monolith.fulfillment.utils.FulfilmentUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class ProductFulfillmentValidator implements FulfilmentValidator{
   private final FulfillmentRepository fulfillmentRepository;

    @Override
    public boolean validate(Fulfillment fulfillment) {
        var uniqueWarehouseIds = FulfilmentUtil.getUniqueIds(fulfillment.getWarehouseIds());
        List<FulfillmentAssociation> existingFulfillment = fulfillmentRepository.findByStoreAndProduct(fulfillment.getStoreId(), fulfillment.getProductId());
        if (existingFulfillment.size() + uniqueWarehouseIds.size() > 2) {
            throw new FulfillmentException(ErrorRule.PRODUCT_FULFILMENT_MAX_WAREHOUSES_PER_STORE_EXCEEDED);
        }
        return true;
    }
}
