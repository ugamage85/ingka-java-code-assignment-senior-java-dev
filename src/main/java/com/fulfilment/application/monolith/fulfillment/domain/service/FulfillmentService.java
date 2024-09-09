package com.fulfilment.application.monolith.fulfillment.domain.service;

import com.fulfilment.application.monolith.exceptions.ErrorRule;
import com.fulfilment.application.monolith.exceptions.FulfillmentException;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentAssociation;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentRepository;
import com.fulfilment.application.monolith.fulfillment.domain.model.Fulfillment;
import com.fulfilment.application.monolith.fulfillment.validatior.FulfilmentValidator;
import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class FulfillmentService {
    private final FulfillmentRepository fulfillmentRepository;
   private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;

    private final List<FulfilmentValidator> validators;

    @Transactional
    public FulfillmentAssociation createFulfillment(Fulfillment fulfillment) {
        Long storeId = fulfillment.getStoreId();
        Long productId = fulfillment.getProductId();
        List<Long> warehouseIds = fulfillment.getWarehouseIds();

        Store store = Store.findById(storeId);
        if(store == null){
            throw new FulfillmentException(ErrorRule.STORE_NOT_FOUND);
        }

        Product product = productRepository.findById(productId);
        if(product == null){
            throw new FulfillmentException(ErrorRule.PRODUCT_NOT_FOUND);
        }

        List<DbWarehouse> warehouses = warehouseRepository.list("id IN ?1", warehouseIds);

        if (isFulfillmentDuplicate(storeId, productId, warehouseIds)) {
            throw new FulfillmentException(ErrorRule.FULFILLMENT_ALREADY_EXIST);
        }


        /*validateProductFulfillment(storeId, productId, warehouseIds);
        validateStoreFulfillment(storeId, warehouseIds);
        validateWarehouseFulfillment(warehouseIds);*/

        validators.stream().allMatch(validator -> validator.validate(fulfillment));

        FulfillmentAssociation fulfillmentAssociation = new FulfillmentAssociation();
        fulfillmentAssociation.setStore(store);
        fulfillmentAssociation.setProduct(product);
        fulfillmentAssociation.setWarehouses(warehouses);
        fulfillmentRepository.persist(fulfillmentAssociation);
        return fulfillmentAssociation;
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
            throw new FulfillmentException(ErrorRule.PRODUCT_FULFILMENT_MAX_WAREHOUSES_PER_STORE_EXCEEDED);
        }
    }

    private void validateStoreFulfillment(Long storeId, List<Long> warehouseIds) {
        var uniqueWarehouseIds = getUniqueWarehouseIds(warehouseIds);
        if (fulfillmentRepository.countByStore(storeId) + uniqueWarehouseIds.size() > 3) {
            throw new FulfillmentException(ErrorRule.STORE_FULFILMENT_MAX_WAREHOUSES_EXCEEDED);
        }
    }

    private void validateWarehouseFulfillment(List<Long> warehouseIds) {
        var uniqueWarehouseIds = getUniqueWarehouseIds(warehouseIds);
        for (Long warehouseId : uniqueWarehouseIds) {
           DbWarehouse warehouse =  warehouseRepository.findById(warehouseId);
            if (warehouse!= null && fulfillmentRepository.countProductsInWarehouse(warehouseId) >= 5) {
                throw new FulfillmentException(ErrorRule.WAREHOUSE_MAX_PRODUCT_TYPES_EXCEEDED);
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
}
