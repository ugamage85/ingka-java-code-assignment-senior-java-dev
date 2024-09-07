package com.fulfilment.application.monolith.fulfillment.adaptors.database;

import com.fulfilment.application.monolith.fulfillment.adaptors.database.DbFulfillmentUnit;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FulfillmentUnitRepository implements PanacheRepository<DbFulfillmentUnit> {}