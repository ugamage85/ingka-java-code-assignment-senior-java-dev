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

    @Test
    @Transactional
    public void testUpdateStore_Successful() {
        // Arrange
        Store store = new Store();
        store.name = "Integration Test Store";
        store.quantityProductsInStock = 200;

        // Act
        // Create a new store
        Integer id = given()
                .contentType(ContentType.JSON)
                .body(store)
                .when()
                .post("/store")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Update the store properties
        store.name = "Updated Integration Test Store";
        store.quantityProductsInStock = 300;

        // Update the store
        given()
                .contentType(ContentType.JSON)
                .body(store)
                .when()
                .put("/store/" + id)
                .then()
                .statusCode(200)
                .body("name", is("Updated Integration Test Store"))
                .body("quantityProductsInStock", is(300));
    }
}


