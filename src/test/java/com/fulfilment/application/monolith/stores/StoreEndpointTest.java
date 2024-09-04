package com.fulfilment.application.monolith.stores;


import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class StoreEndpointTest {
    @Test
    @Transactional
    public void testCreateStore_Successful() {
        // Arrange
        Store store = new Store();
        store.name = "Integration Test Store";
        store.quantityProductsInStock = 200;

        // Act & Assert
        given()
                .contentType(ContentType.JSON)
                .body(store)
                .when()
                .post("/store")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", is("Integration Test Store"))
                .body("quantityProductsInStock", is(200));
    }
}


