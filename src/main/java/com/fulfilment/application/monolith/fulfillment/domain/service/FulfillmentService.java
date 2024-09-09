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

    public void deleteFulfillment(Long id) {
        FulfillmentAssociation fulfillment = fulfillmentRepository.findById(id);
        if (fulfillment != null) {
            fulfillmentRepository.delete(fulfillment);
        } else {
            throw new IllegalArgumentException("Fulfillment with id " + id + " does not exist");
        }
    }

    public List<FulfillmentAssociation> getAllFulfillments() {
        return fulfillmentRepository.listAll();
    }

}
