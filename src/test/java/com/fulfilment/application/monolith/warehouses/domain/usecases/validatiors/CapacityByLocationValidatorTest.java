package com.fulfilment.application.monolith.warehouses.domain.usecases.validatiors;

import com.fulfilment.application.monolith.exceptions.WarehouseException;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.validator.CapacityByLocationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class CapacityByLocationValidatorTest {

    @Mock
    private LocationResolver locationResolver;

    private CapacityByLocationValidator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new CapacityByLocationValidator(locationResolver);
    }

    @Test
    public void testValidate_WhenWarehouseCapacityIsLessThanLocationMaxCapacity_ShouldReturnTrue() {
        // Arrange
        Warehouse warehouse = new Warehouse();
        warehouse.setCapacity(50);
        warehouse.setLocation("location1");

        Location location = new Location();
        location.setMaxCapacity(100);

        when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(location);

        // Act
        boolean result = validator.validate(warehouse);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testValidate_WhenWarehouseCapacityIsGreaterThanLocationMaxCapacity_ShouldThrowException() {
        // Arrange
        Warehouse warehouse = new Warehouse();
        warehouse.setCapacity(200);
        warehouse.setLocation("location1");

        Location location = new Location();
        location.setMaxCapacity(100);

        when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(location);

        // Act & Assert
        assertThrows(WarehouseException.class, () -> validator.validate(warehouse));
    }
}