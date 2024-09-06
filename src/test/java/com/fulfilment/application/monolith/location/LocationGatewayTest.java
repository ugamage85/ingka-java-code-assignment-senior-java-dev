package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import jakarta.ws.rs.WebApplicationException;
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
    WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
      locationGateway.resolveByIdentifier("ZWOLLE-003");
    });
  }

  @Test
  public void shouldThrowExceptionWhenLocationIdentifierIsNull() {
    //when
    WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
      locationGateway.resolveByIdentifier(null);
    });
  }

  @Test
  public void shouldThrowExceptionWhenLocationIdentifierIsAnEmptyString() {
    //when
    WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
      locationGateway.resolveByIdentifier("");
    });
  }
}
