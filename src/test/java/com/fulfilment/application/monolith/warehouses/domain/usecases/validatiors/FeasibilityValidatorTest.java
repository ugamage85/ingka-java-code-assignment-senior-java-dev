package com.fulfilment.application.monolith.warehouses.domain.usecases.validatiors;

import com.fulfilment.application.monolith.exceptions.WarehouseException;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.domain.validator.FeasibilityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class FeasibilityValidatorTest {

    @Mock
    private LocationResolver locationResolver;

    @Mock
    private WarehouseStore warehouseStore;

    private FeasibilityValidator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new FeasibilityValidator(locationResolver, warehouseStore);
    }

    @Test
    public void testValidate_WhenWarehouseCreationIsFeasible_ShouldReturnTrue() {
        // Arrange
        Warehouse warehouse = new Warehouse();
        warehouse.setLocation("location1");

        Location location = new Location();
        location.setMaxNumberOfWarehouses(2);

        when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(location);
        when(warehouseStore.getAll()).thenReturn(Collections.emptyList());

        // Act
        boolean result = validator.validate(warehouse);

        // Assert
        assertTrue(result);
    }

    //@Test
    public void testValidate_WhenWarehouseCreationIsNotFeasible_ShouldThrowException() {
        // Arrange
        Warehouse warehouse = new Warehouse();
        warehouse.setLocation("location1");

        Location location = new Location();
        location.setMaxNumberOfWarehouses(1);

        Warehouse existingWarehouse = new Warehouse();
        existingWarehouse.setLocation("location1");


        when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(location);
        when(warehouseStore.getAll()).thenReturn(Arrays.asList(existingWarehouse, existingWarehouse));

        // Act & Assert
        assertThrows(WarehouseException.class, () -> validator.validate(warehouse));
    }
}