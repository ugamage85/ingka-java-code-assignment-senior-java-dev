package com.fulfilment.application.monolith.fulfillment.adaptors.database;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class DbFulfillmentUnit {

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public DbWarehouse warehouse;

    @ManyToOne
    public Product product;

    @ManyToOne
    public Store store;

}