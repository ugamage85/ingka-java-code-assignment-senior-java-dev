package com.fulfilment.application.monolith.fulfillment.ports;

import com.fulfilment.application.monolith.fulfillment.adaptors.database.DbFulfillmentUnit;

public interface ProductFulfilmentOperation {
    DbFulfillmentUnit createFulfillmentUnit(DbFulfillmentUnit fulfillmentUnit);
}
