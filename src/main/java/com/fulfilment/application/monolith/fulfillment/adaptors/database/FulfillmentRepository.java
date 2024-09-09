package com.fulfilment.application.monolith.fulfillment.adaptors.database;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FulfillmentRepository implements PanacheRepository<FulfillmentAssociation> {
    public long countByStore(Long storeId) {
        return count("store.id", storeId);
    }

    public long countByProductInWarehouse(Long productId, Long warehouseId) {
       // return count("product.id = ?1 AND warehouses.id = ?2", productId, warehouseId);
        return count("product.id = :productId AND warehouses.id = :warehouseId",
                Parameters.with("productId", productId).and("warehouseId", warehouseId));
    }

    public List<FulfillmentAssociation> findByStoreAndProduct(Long storeId, Long productId) {
        //return find("store.id = ?1 AND product.id = ?2", storeId, productId).list();
        return find("store.id = :storeId AND product.id = :productId",
                Parameters.with("storeId", storeId).and("productId", productId)).list();
    }

    public long countProductsInWarehouse(Long warehouseId) {
        //return this.count("SELECT COUNT(DISTINCT f.product.id) FROM FulfillmentAssociation f JOIN f.warehouses w WHERE w.id = ?1", warehouseId);
        return count("SELECT COUNT(DISTINCT f.product.id) FROM FulfillmentAssociation f JOIN f.warehouses w WHERE w.id = :warehouseId",
                Parameters.with("warehouseId", warehouseId));
    }

}