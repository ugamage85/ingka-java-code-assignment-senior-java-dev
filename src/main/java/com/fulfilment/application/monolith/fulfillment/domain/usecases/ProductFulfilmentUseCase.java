package com.fulfilment.application.monolith.fulfillment.domain.usecases;

import com.fulfilment.application.monolith.fulfillment.adaptors.database.DbFulfillmentUnit;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentUnitRepository;
import com.fulfilment.application.monolith.fulfillment.ports.ProductFulfilmentOperation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class ProductFulfilmentUseCase implements ProductFulfilmentOperation {
    private  final FulfillmentUnitRepository fulfillmentUnitRepository;

    @Override
    public DbFulfillmentUnit createFulfillmentUnit(DbFulfillmentUnit fulfillmentUnit) {
        List<DbFulfillmentUnit> existingFulfillmentUnitsForProduct = fulfillmentUnitRepository.list("product = ?1 and store = ?2", fulfillmentUnit.getProduct(), fulfillmentUnit.getStore());
        if (existingFulfillmentUnitsForProduct.size() >= 2) {
            throw new WebApplicationException("Each Product can be fulfilled by a maximum of 2 different Warehouses per Store", 400);
        }

        List<DbFulfillmentUnit> existingFulfillmentUnitsForStore = fulfillmentUnitRepository.list("store = ?1", fulfillmentUnit.getStore());
        if (existingFulfillmentUnitsForStore.size() >= 3) {
            throw new WebApplicationException("Each Store can be fulfilled by a maximum of 3 different Warehouses", 400);
        }

        List<DbFulfillmentUnit> existingFulfillmentUnitsForWarehouse = fulfillmentUnitRepository.list("warehouse = ?1", fulfillmentUnit.getWarehouse());
        if (existingFulfillmentUnitsForWarehouse.size() >= 5) {
            throw new WebApplicationException("Each Warehouse can store maximally 5 types of Products", 400);
        }

        fulfillmentUnitRepository.persist(fulfillmentUnit);
        return fulfillmentUnit;
    }
}
