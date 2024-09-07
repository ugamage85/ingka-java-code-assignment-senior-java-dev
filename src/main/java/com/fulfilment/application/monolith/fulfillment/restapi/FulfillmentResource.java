package com.fulfilment.application.monolith.fulfillment.restapi;

import com.fulfilment.application.monolith.fulfillment.adaptors.database.DbFulfillmentUnit;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentUnitRepository;
import com.fulfilment.application.monolith.fulfillment.ports.ProductFulfilmentOperation;
import com.fulfilment.application.monolith.products.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@Path("fulfillment")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FulfillmentResource {
    @Inject
    ProductFulfilmentOperation productFulfilmentOperation;
    @Inject
    FulfillmentUnitRepository fulfillmentUnitRepository;

    @POST
    @Transactional
    public Response create(DbFulfillmentUnit fulfillmentUnit) {
        if (fulfillmentUnit.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

      productFulfilmentOperation.createFulfillmentUnit(fulfillmentUnit);
        return Response.ok(fulfillmentUnit).status(201).build();
    }
}
