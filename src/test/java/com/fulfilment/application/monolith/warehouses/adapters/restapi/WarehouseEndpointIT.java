package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@QuarkusIntegrationTest
public class WarehouseEndpointIT {
  private static final String PATH = "warehouse";

  @Test
  public void testSimpleListWarehouses() {
    // List all, should have all 3 products the database has initially:
    given()
            .when()
            .get(PATH)
            .then()
            .statusCode(200)
            .body(containsString("MWH.001"), containsString("MWH.012"), containsString("MWH.023"));
  }

  @Test
  public void testGetAWarehouseUnitByID() {
    String warehouseId = "2";
    // Send the GET request to get the warehouse
    given()
            .contentType(ContentType.JSON)
            .when()
            .get(PATH +"/"+ warehouseId)
            .then()
            .statusCode(200)
            .body("businessUnitCode", equalTo("MWH.012"))
            .body("location", equalTo("AMSTERDAM-001"))
            .body("capacity", equalTo(50))
            .body("stock", equalTo(5));
  }

  @Test
  @Transactional
  public void testCreateANewWarehouseUnit() {
    createNewWarehouse("MWH.101", "AMSTERDAM-001", 90, 50);
  }

  @Test
  public void testSimpleCheckingArchivingWarehouses() {
    // List all, should have all 3 products the database has initially:
    given()
            .when()
            .get(PATH)
            .then()
            .statusCode(200)
            .body(
                    containsString("MWH.001"),
                    containsString("MWH.012"),
                    containsString("MWH.023"),
                    containsString("ZWOLLE-001"),
                    containsString("AMSTERDAM-001"),
                    containsString("TILBURG-001"));

    // Archive the ZWOLLE-001:
    given().when().delete(PATH + "/1").then().statusCode(204);

    // List all, ZWOLLE-001 should be missing now:
    given()
            .when()
            .get(PATH)
            .then()
            .statusCode(200)
            .body(
                    not(containsString("ZWOLLE-001")),
                    containsString("AMSTERDAM-001"),
                    containsString("TILBURG-001"));
  }

  @Test
  @Transactional
  public void testReplaceTheCurrentActiveWarehouse() {
    // create a new warehouse
    createNewWarehouse("MWH.102", "AMSTERDAM-001", 90, 50);

    // Replace the warehouse (see capacity and stock)
    replaceWarehouse("MWH.102", "AMSTERDAM-001", 80, 50);
  }

  private void createNewWarehouse(String businessUnitCode, String location, int capacity, int stock) {
    String newWarehouseJson = "{"
            + "\"businessUnitCode\": \"" + businessUnitCode + "\","
            + "\"location\": \"" + location + "\","
            + "\"capacity\": " + capacity + ","
            + "\"stock\": " + stock
            + "}";

    given()
            .contentType(ContentType.JSON)
            .body(newWarehouseJson)
            .when()
            .post(PATH)
            .then()
            .statusCode(200)
            .body("businessUnitCode", equalTo(businessUnitCode))
            .body("location", equalTo(location))
            .body("capacity", equalTo(capacity))
            .body("stock", equalTo(stock));
  }

  private void replaceWarehouse(String businessUnitCode, String location, int capacity, int stock) {
    String replacingWarehouseJson = "{"
            + "\"businessUnitCode\": \"" + businessUnitCode + "\","
            + "\"location\": \"" + location + "\","
            + "\"capacity\": " + capacity + ","
            + "\"stock\": " + stock
            + "}";

    given()
            .contentType(ContentType.JSON)
            .body(replacingWarehouseJson)
            .when()
            .post(PATH + "/" + businessUnitCode + "/replacement")
            .then()
            .statusCode(200)
            .body("businessUnitCode", equalTo(businessUnitCode))
            .body("location", equalTo(location))
            .body("capacity", equalTo(capacity))
            .body("stock", equalTo(stock));
  }
}
