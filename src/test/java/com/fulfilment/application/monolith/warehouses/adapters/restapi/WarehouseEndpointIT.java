package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.warehouse.api.beans.Warehouse;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusIntegrationTest
public class WarehouseEndpointIT {

  @Test
  public void testSimpleListWarehouses() {

    final String path = "warehouse";

    // List all, should have all 3 products the database has initially:
    given()
            .when()
            .get(path)
            .then()
            .statusCode(200)
            .body(containsString("MWH.001"), containsString("MWH.012"), containsString("MWH.023"));
  }

  @Test
  public void shouldFindWarehouseById() {

    final String path = "warehouse";
    //(1, 'MWH.001', 'ZWOLLE-001', 100, 10, '2024-07-01', null)
    //given().when().get(path + "/1").then().statusCode(200);

    // List all, should have all 3 products the database has initially:
    var response = given()
            .when()
            .get(path)
            .then()
            .extract().response();
    assertEquals(200, response.getStatusCode());
    //.statusCode(200)
    //.body(containsString("MWH.001"));
  }

  @Test
  @Transactional
  public void shouldCreateWarehouse() {

    final String path = "warehouse";
    Warehouse warehouse = new Warehouse();
    warehouse.setBusinessUnitCode("MWH.100");
    warehouse.setLocation("AMSTERDAM-001");
    warehouse.setCapacity(30);
    warehouse.setStock(10);

    given()
            .contentType(ContentType.JSON)
            .body(warehouse)
            .when()
            .post(path)
            .then()
            .statusCode(200);
    //.body("id", notNullValue())
    //.body("name", is("Integration Test Store"))
    //.body("quantityProductsInStock", is(200));

  }

  @Test
  public void testSimpleCheckingArchivingWarehouses() {
    final String path = "warehouse";

    // List all, should have all 3 products the database has initially:
    given()
            .when()
            .get(path)
            .then()
            .statusCode(200)
            .body(
                    containsString("MWH.001"),
                    containsString("MWH.012"),
                    containsString("MWH.023"),
                    containsString("ZWOLLE-001"),
                    containsString("AMSTERDAM-001"),
                    containsString("TILBURG-001"));

    // // Archive the ZWOLLE-001:
    given().when().delete(path + "/1").then().statusCode(204);

    // // List all, ZWOLLE-001 should be missing now:
    given()
            .when()
            .get(path)
            .then()
            .statusCode(200)
            .body(
                    not(containsString("ZWOLLE-001")),
                    containsString("AMSTERDAM-001"),
                    containsString("TILBURG-001"));
  }

  //@Test todo: fix this test
  public void testReplaceTheCurrentActiveWarehouse() {
    // Given
    //RestAssured.baseURI = "http://localhost:8080"; // replace with your application's base URI
    String businessUnitCode = "MWH.012"; // replace with the business unit code of the warehouse to replace
    String requestBody = "{"
            + "\"businessUnitCode\":\"MWH.012\","
            + "\"location\":\"AMSTERDAM-001\","
            + "\"capacity\":30,"
            + "\"stock\":5"
            + "}"; // replace with the data of the new warehouse

    // When
    Response response = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post("/warehouse/" + businessUnitCode + "/replacement");

    // Then
    response.then()
            .statusCode(200);
            //.body("businessUnitCode", equalTo("MWH.001"))
            //.body("location", equalTo("TILBURG-001"));
    //.body("capacity", equalTo(30));
    //.body("stock", equalTo(27));
  }
}
