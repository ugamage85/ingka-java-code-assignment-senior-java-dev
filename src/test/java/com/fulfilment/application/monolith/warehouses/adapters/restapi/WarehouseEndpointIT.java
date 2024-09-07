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

  //@Test
  public void testGetAWarehouseUnitByID() {
    // Define the warehouse id
    String warehouseId = "1";

    // Send the GET request to get the warehouse
    given()
            .contentType(ContentType.JSON)
            .when()
            .get("/warehouse/" + warehouseId)
            .then()
            .statusCode(200)
            .body("businessUnitCode", equalTo("MWH.001"))
            .body("location", equalTo("ZWOLLE-001"))
            .body("capacity", equalTo(100))
            .body("stock", equalTo(10));
  }

  @Test
  @Transactional
  public void testCreateANewWarehouseUnit() {
    String newWarehouseJson = "{"
            + "\"businessUnitCode\": \"MWH.101\","
            + "\"location\": \"AMSTERDAM-001\","
            + "\"capacity\": 90,"
            + "\"stock\": 50"
            + "}";

    // Send the POST request to create the warehouse
    given()
            .contentType(ContentType.JSON)
            .body(newWarehouseJson)
            .when()
            .post("/warehouse")
            .then()
            .statusCode(200)
            .body("businessUnitCode", equalTo("MWH.101"))
            .body("location", equalTo("AMSTERDAM-001"))
            .body("capacity", equalTo(90))
            .body("stock", equalTo(50));
    //.body("id", notNullValue())

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

  @Test
  public void testReplaceTheCurrentActiveWarehouse() {
    // create a new warehouse
    String newWarehouseJson = "{"
            + "\"businessUnitCode\": \"MWH.102\","
            + "\"location\": \"AMSTERDAM-001\","
            + "\"capacity\": 90,"
            + "\"stock\": 50"
            + "}";

    // Send the POST request to create the warehouse
    given()
            .contentType(ContentType.JSON)
            .body(newWarehouseJson)
            .when()
            .post("/warehouse")
            .then()
            .statusCode(200);

    // Replace the warehouse
    String replacingWarehouseJson = "{"
            + "\"businessUnitCode\": \"MWH.102\","
            + "\"location\": \"AMSTERDAM-001\","
            + "\"capacity\": 90,"
            + "\"stock\": 50"
            + "}";

    // Send the POST request to replace the warehouse
   given()
            .contentType(ContentType.JSON)
            .body(replacingWarehouseJson)
            .when()
            .post("/warehouse/MWH.102/replacement")
            .then()
            .statusCode(200)
            .body("businessUnitCode", equalTo("MWH.102"))
            .body("location", equalTo("AMSTERDAM-001"))
            .body("capacity", equalTo(90))
            .body("stock", equalTo(50));
  }
}
