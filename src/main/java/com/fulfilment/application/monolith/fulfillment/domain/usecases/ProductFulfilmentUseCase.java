package com.fulfilment.application.monolith.fulfillment.domain.usecases;

import com.fulfilment.application.monolith.fulfillment.adaptors.database.DbFulfillmentUnit;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentUnitRepository;
import com.fulfilment.application.monolith.fulfillment.ports.ProductFulfilmentOperation;
import com.fulfilment.application.monolith.mapper.WarehouseMapper;
import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class ProductFulfilmentUseCase implements ProductFulfilmentOperation {
    private  final FulfillmentUnitRepository fulfillmentUnitRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public void createFulfillmentUnit(DbFulfillmentUnit fulfillmentUnit) {
        Product product = productRepository.find("name", fulfillmentUnit.getProduct().name).firstResult();
        if (product == null) {
            throw new WebApplicationException("Product with id " + fulfillmentUnit.getProduct().id + " does not exist.", 404);
        }

        Store store = Store.find("name", fulfillmentUnit.getStore().name).firstResult();
        if (store == null) {
            throw new WebApplicationException("Store with name " + fulfillmentUnit.getStore().name + " does not exist.", 404);
        }

        DbWarehouse dbWarehouse = warehouseRepository.find("businessUnitCode", fulfillmentUnit.getWarehouse().getBusinessUnitCode()).firstResult();
        if (dbWarehouse == null) {
            throw new WebApplicationException("Warehouse with business unit code " + fulfillmentUnit.getWarehouse().getBusinessUnitCode() + " does not exist.", 404);
        }
        // Set the fetched product,store,warehouse to fulfillmentUnit
        fulfillmentUnit.setProduct(product);
        fulfillmentUnit.setStore(store);
        fulfillmentUnit.setWarehouse(dbWarehouse);

        List<DbFulfillmentUnit> existingFulfillmentUnitsForSpecificProductAndStore = fulfillmentUnitRepository.list("product.name = ?1 and store.name = ?2", fulfillmentUnit.getProduct().name, fulfillmentUnit.getStore().name);

        // Extract unique warehouse IDs
        Set<Long> uniqueWarehouseIds = existingFulfillmentUnitsForSpecificProductAndStore.stream()
                .map(unit -> unit.getWarehouse().getId())
                .collect(Collectors.toSet());

        if (uniqueWarehouseIds.size() >= 2) {
            throw new WebApplicationException("Each Product can be fulfilled by a maximum of 2 different Warehouses per Store", 400);
        }

        /*List<DbFulfillmentUnit> existingFulfillmentUnitsForProduct = fulfillmentUnitRepository.list("product.name = ?1 and store.name = ?2", fulfillmentUnit.getProduct().name, fulfillmentUnit.getStore().name);
        if (existingFulfillmentUnitsForProduct.size() >= 2) {
            throw new WebApplicationException("Each Product can be fulfilled by a maximum of 2 different Warehouses per Store", 400);
        }*/

        List<DbFulfillmentUnit> existingFulfillmentUnitsForStore = fulfillmentUnitRepository.list("store.name = ?1", fulfillmentUnit.getStore().name);
        if (existingFulfillmentUnitsForStore.size() >= 3) {
            throw new WebApplicationException("Each Store can be fulfilled by a maximum of 3 different Warehouses", 400);
        }

        List<DbFulfillmentUnit> existingFulfillmentUnitsForWarehouse = fulfillmentUnitRepository.list("warehouse.businessUnitCode = ?1", fulfillmentUnit.getWarehouse().getBusinessUnitCode());
        if (existingFulfillmentUnitsForWarehouse.size() >= 5) {
            throw new WebApplicationException("Each Warehouse can store maximally 5 types of Products", 400);
        }
        // Fetch the product from the database

        fulfillmentUnitRepository.persist(fulfillmentUnit);
    }

}