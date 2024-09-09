package com.fulfilment.application.monolith.fulfillment;

import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@QuarkusTest
public class FulfillmentResourceTest {
    private static final String PATH = "/fulfillments";
    @Test
    public void testCreateFulfillment() {
        FulfillmentRequest request = new FulfillmentRequest(1L, 2L, Arrays.asList(1L, 2L));
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(PATH)
                .then()
                .statusCode(201)
                .body("store.id", equalTo(1))
                .body("store.name", equalTo("TONSTAD"))
                .body("store.quantityProductsInStock", equalTo(10))
                .body("product.id", equalTo(2))
                .body("product.name", equalTo("KALLAX"))
                .body("product.description", equalTo(null))
                .body("product.price", equalTo(null))
                .body("product.stock", equalTo(5))
                .body("warehouses.id", hasItems(1, 2))
                .body("warehouses.businessUnitCode", hasItems("MWH.001", "MWH.012"))
                .body("warehouses.location", hasItems("ZWOLLE-001", "AMSTERDAM-001"))
                .body("warehouses.capacity", hasItems(100, 50))
                .body("warehouses.stock", hasItems(10, 5));
    }

   @Test
    public void testGetFulfillments() {

      var result =  given()
                .queryParam("storeId", 1)
                .queryParam("productId", 2)
                .when()
                .get(PATH)
                .then()
                .statusCode(200)
               .body("[0].id", equalTo(1))
               .body("[0].store.id", equalTo(1))
               .body("[0].store.name", equalTo("TONSTAD"))
               .body("[0].store.quantityProductsInStock", equalTo(10))
               .body("[0].product.id", equalTo(2))
               .body("[0].product.name", equalTo("KALLAX"))
               .body("[0].product.description", equalTo(null))
               .body("[0].product.price", equalTo(null))
               .body("[0].product.stock", equalTo(5))
               .body("[0].warehouses.id", hasItems(1, 2))
               .body("[0].warehouses.businessUnitCode", hasItems("MWH.001", "MWH.012"))
               .body("[0].warehouses.location", hasItems("ZWOLLE-001", "AMSTERDAM-001"))
               .body("[0].warehouses.capacity", hasItems(100, 50))
               .body("[0].warehouses.stock", hasItems(10, 5));
    }

    @Test
    public void testDeleteFulfillmentWithInvalidIdReturns404() {
        // Assume we are deleting a fulfillment with ID 1
        Long fulfillmentId = 1L;

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/fulfillments/{id}", fulfillmentId)
                .then()
                .statusCode(404);
    }
}
