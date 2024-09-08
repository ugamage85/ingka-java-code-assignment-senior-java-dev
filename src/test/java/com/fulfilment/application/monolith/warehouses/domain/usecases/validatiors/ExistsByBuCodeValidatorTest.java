package com.fulfilment.application.monolith.warehouses.domain.usecases.validatiors;

import com.fulfilment.application.monolith.exceptions.WarehouseException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.domain.validator.ExistsByBuCodeValidator;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ExistsByBuCodeValidatorTest {

    @Mock
    private WarehouseStore warehouseStore;

    private ExistsByBuCodeValidator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ExistsByBuCodeValidator(warehouseStore);
    }

    @Test
    public void testValidate_WhenWarehouseExists_ShouldThrowWebApplicationException() {
        // Arrange
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("BU1");

        when(warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(new Warehouse());

        // Act & Assert
        assertThrows(WebApplicationException.class, () -> validator.validate(warehouse));
    }

    //@Test
    public void testValidate_WhenWarehouseDoesNotExist_ShouldReturnTrue() {
        // Arrange
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("BU1");

        when(warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenThrow(WarehouseException.class);

        // Act
        boolean result = validator.validate(warehouse);

        // Assert
        assertTrue(result);
    }
}