package com.fulfilment.application.monolith.fulfillment.adaptors.restapi;

import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentRequest;
import com.fulfilment.application.monolith.fulfillment.domain.service.FulfillmentService;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentAssociation;
import com.fulfilment.application.monolith.fulfillment.domain.model.Fulfillment;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/fulfillments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FulfillmentResource {
    @Inject
    FulfillmentService fulfillmentService;

    @POST
    public Response createFulfillment(FulfillmentRequest request) {
        Fulfillment fulfillment = toModel(request);
        FulfillmentAssociation fulfillmentAssociation = fulfillmentService.createFulfillment(fulfillment);
        return Response.status(Response.Status.CREATED).entity(fulfillment).build();
    }

    @GET
    public List<FulfillmentAssociation> getFulfillments(@QueryParam("storeId") Long storeId,
                                                        @QueryParam("productId") Long productId) {
        return fulfillmentService.getFulfillments(storeId, productId);
    }

    private Fulfillment toModel(FulfillmentRequest request) {
        return new Fulfillment(request.getStoreId(), request.getProductId(), request.getWarehouseIds());
    }
}
