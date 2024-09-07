package com.fulfilment.application.monolith.fulfillmentassociation.entities;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
@ApplicationScoped
public class FulfillmentRepository implements PanacheRepository<FulfillmentAssociation>{
    public long countByStore(Long storeId) {
        return count("store.id", storeId);
    }

    public long countByProductInWarehouse(Long productId, Long warehouseId) {
        return count("product.id = ?1 AND warehouses.id = ?2", productId, warehouseId);
    }

    public List<FulfillmentAssociation> findByStoreAndProduct(Long storeId, Long productId) {
        return find("store.id = ?1 AND product.id = ?2", storeId, productId).list();
    }
}
