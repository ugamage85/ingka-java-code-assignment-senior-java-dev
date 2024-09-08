package com.fulfilment.application.monolith.fulfillmentassociation.entities;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class FulfillmentService {

    @Inject
    FulfillmentRepository fulfillmentRepository;

    @Inject
    WarehouseRepository warehouseRepository;

    @Inject
    ProductRepository productRepository;

    @Transactional
    public FulfillmentAssociation createFulfillment(Long storeId, Long productId, List<Long> warehouseIds) {
        Store store = Store.findById(storeId);
        Product product = productRepository.findById(productId);
        List<DbWarehouse> warehouses = warehouseRepository.list("id IN ?1", warehouseIds);

        if (isFulfillmentDuplicate(storeId, productId, warehouseIds)) {
            throw new IllegalStateException("Fulfillment already exists.");
        }


        validateProductFulfillment(storeId, productId, warehouseIds);
        validateStoreFulfillment(storeId, warehouseIds);
        validateWarehouseFulfillment(warehouseIds);

       /* if (warehouses.size() > 2) {
            throw new IllegalStateException("A product can be fulfilled by up to 2 warehouses per store.");
        }

        if (fulfillmentRepository.countByStore(storeId) >= 3) {
            throw new IllegalStateException("A store can be fulfilled by up to 3 warehouses.");
        }

        for (DbWarehouse warehouse : warehouses) {
            if (warehouseRepository.countProductsInWarehouse(warehouse.id) >= 5) {
                throw new IllegalStateException("Warehouse can store up to 5 types of products.");
            }
        }*/

        FulfillmentAssociation fulfillment = new FulfillmentAssociation();
        fulfillment.setStore(store);
        fulfillment.setProduct(product);
        fulfillment.setWarehouses(warehouses);

        fulfillmentRepository.persist(fulfillment);

        return fulfillment;
    }

    public List<FulfillmentAssociation> getFulfillments(Long storeId, Long productId) {
        if (storeId != null && productId != null) {
            return fulfillmentRepository.findByStoreAndProduct(storeId, productId);
        } else {
            return fulfillmentRepository.listAll();
        }
    }
    private void validateProductFulfillment(Long storeId, Long productId, List<Long> warehouseIds) {
        var uniqueWarehouseIds = getUniqueWarehouseIds(warehouseIds);
        List<FulfillmentAssociation> existingFulfillments = fulfillmentRepository.findByStoreAndProduct(storeId, productId);
        if (existingFulfillments.size() + uniqueWarehouseIds.size() > 2) {
            throw new IllegalStateException("A product can be fulfilled by up to 2 warehouses per store.");
        }
    }

    private void validateStoreFulfillment(Long storeId, List<Long> warehouseIds) {
        var uniqueWarehouseIds = getUniqueWarehouseIds(warehouseIds);
        if (fulfillmentRepository.countByStore(storeId) + uniqueWarehouseIds.size() > 3) {
            throw new IllegalStateException("A store can be fulfilled by up to 3 warehouses.");
        }
    }

    private void validateWarehouseFulfillment(List<Long> warehouseIds) {
        var uniqueWarehouseIds = getUniqueWarehouseIds(warehouseIds);
        for (Long warehouseId : uniqueWarehouseIds) {
            if (fulfillmentRepository.countProductsInWarehouse(warehouseId) >= 5) {
                throw new IllegalStateException("Warehouse can store up to 5 types of products.");
            }
        }
    }

    private Set<Long> getUniqueWarehouseIds(List<Long> warehouseIds) {
        Set<Long> uniqueWarehouseIds = new HashSet<>(warehouseIds);
        //return uniqueWarehouseIds.size() == warehouseIds.size();
        return uniqueWarehouseIds;
    }

    private boolean isFulfillmentDuplicate(Long storeId, Long productId, List<Long> warehouseIds) {
        var uniqueWarehouseIds = getUniqueWarehouseIds(warehouseIds);
        List<FulfillmentAssociation> existingFulfillments = fulfillmentRepository.findByStoreAndProduct(storeId, productId);
        for (FulfillmentAssociation existingFulfillment : existingFulfillments) {
            if (existingFulfillment.getWarehouses().stream().map(DbWarehouse::getId).toList().containsAll(uniqueWarehouseIds)) {
                return true;
            }
        }
        return false;
    }

    // Methods for retrieving, updating, and deleting fulfillment associations
}


