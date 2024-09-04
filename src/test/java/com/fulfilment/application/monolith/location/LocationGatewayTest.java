package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.LocationGatewayException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocationGatewayTest {
  private LocationGateway locationGateway;

  @BeforeEach
  public void setUp() {
    //given
    locationGateway = new LocationGateway();
  }

  @Test
  public void testWhenResolveExistingLocationShouldReturn() {
    //when
    Location location = locationGateway.resolveByIdentifier("ZWOLLE-001");

    //then
    assertNotNull(location, "Location should not be null");
    assertEquals(location.getIdentification(), "ZWOLLE-001");
    assertEquals("ZWOLLE-001", location.getIdentification(), "The identifier should match the expected value");
    assertEquals(1, location.getMaxNumberOfWarehouses(), "The max number of warehouses should match the expected value");
    assertEquals(40, location.getMaxCapacity(), "The max capacity should match the expected value");
  }

  @Test
  public void shouldThrowExceptionWhenLocationIdentifierIsNotPresence() {
    //when
    LocationGatewayException exception = assertThrows(LocationGatewayException.class, () -> {
      locationGateway.resolveByIdentifier("ZWOLLE-003");
    });

    //then
    assertEquals("location,ZWOLLE-003 not found", exception.getMessage(), "Exception message should match the expected value");
  }

  @Test
  public void shouldThrowExceptionWhenLocationIdentifierIsNull() {
    //when
    LocationGatewayException exception = assertThrows(LocationGatewayException.class, () -> {
      locationGateway.resolveByIdentifier(null);
    });

    //then
    assertEquals("location,null not found", exception.getMessage(), "Exception message should match the expected value");
  }

  @Test
  public void shouldThrowExceptionWhenLocationIdentifierIsAnEmptyString() {
    //when
    LocationGatewayException exception = assertThrows(LocationGatewayException.class, () -> {
      locationGateway.resolveByIdentifier("");
    });

    //then
    assertEquals("location, not found", exception.getMessage(), "Exception message should match the expected value");
  }
}
