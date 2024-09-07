package com.fulfilment.application.monolith.fulfillmentassociation.entities;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fulfillments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FulfillmentResource {
    @Inject
    FulfillmentService fulfillmentService;

    @POST
    public Response createFulfillment(FulfillmentRequest request) {
        FulfillmentAssociation fulfillment = fulfillmentService.createFulfillment(
                request.getStoreId(), request.getProductId(), request.getWarehouseIds()
        );
        return Response.status(Response.Status.CREATED).entity(fulfillment).build();
    }
}
