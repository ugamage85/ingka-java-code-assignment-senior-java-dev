package com.fulfilment.application.monolith.fulfillment;

import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentRequest;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;

@QuarkusIntegrationTest
public class FulfillmentResourceTest {

    //@Test
    public void testCreateFulfillment() {
        var result = given()
                .contentType(ContentType.JSON)
                .body(new FulfillmentRequest(1L, 1L, Arrays.asList(1L, 2L)))
                .when()
                .post("/fulfillments")
                .then()
                .statusCode(201)
                .body("storeId", equalTo(1))
                .body("productId", equalTo(1))
                .body("warehouseIds", hasItems(1, 2));
    }

    //@Test
    public void testGetFulfillments() {

        given()
                .queryParam("storeId", 1)
                .queryParam("productId", 1)
                .when()
                .get("/fulfillments")
                .then()
                .statusCode(200)
                .body("storeId", everyItem(equalTo(1)))
                .body("productId", everyItem(equalTo(1)));
    }
}
