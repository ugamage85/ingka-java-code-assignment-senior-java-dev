package com.fulfilment.application.monolith.fulfillmentassociation.entities;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class FulfillmentService {

    @Inject
    FulfillmentRepository fulfillmentRepository;

    @Inject
    WarehouseRepository warehouseRepository;

    //@Inject
    //Store storeRepository;

    @Inject
    ProductRepository productRepository;

    @Transactional
    public FulfillmentAssociation createFulfillment(Long storeId, Long productId, List<Long> warehouseIds) {
        Store store = Store.findById(storeId);
        Product product = productRepository.findById(productId);
        List<DbWarehouse> warehouses = warehouseRepository.list("id IN ?1", warehouseIds);

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

    // Methods for retrieving, updating, and deleting fulfillment associations
}
