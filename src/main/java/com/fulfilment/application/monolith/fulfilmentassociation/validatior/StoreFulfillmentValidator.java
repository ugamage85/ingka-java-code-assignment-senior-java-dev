package com.fulfilment.application.monolith.fulfilmentassociation.validatior;

import com.fulfilment.application.monolith.exceptions.ErrorRule;
import com.fulfilment.application.monolith.exceptions.FulfillmentException;
import com.fulfilment.application.monolith.fulfilmentassociation.FulfillmentRepository;
import com.fulfilment.application.monolith.fulfilmentassociation.domain.Fulfillment;
import com.fulfilment.application.monolith.fulfilmentassociation.utils.FulfilmentUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@ApplicationScoped
public class StoreFulfillmentValidator implements FulfilmentValidator {
    private final FulfillmentRepository fulfillmentRepository;

    @Override
    public boolean validate(Fulfillment fulfillment) {
        var uniqueWarehouseIds = FulfilmentUtil.getUniqueIds(fulfillment.getWarehouseIds());
        if (fulfillmentRepository.countByStore(fulfillment.getStoreId()) + uniqueWarehouseIds.size() > 3) {
            throw new FulfillmentException(ErrorRule.STORE_FULFILMENT_MAX_WAREHOUSES_EXCEEDED);
        }
        return true;
    }
}
