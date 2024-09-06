package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReplaceWarehouseUseCaseTest {

    @Mock
    private WarehouseStore warehouseStore;

    @Mock
    private ArchiveWarehouseOperation archiveWarehouseOperation;

    @InjectMocks
    private ReplaceWarehouseUseCase replaceWarehouseUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should replace warehouse when all conditions are met")
    public void shouldReplaceWarehouse() {
        Warehouse existingWarehouse = new Warehouse("BU-001", "ZWOLLE-001", 50, 30, LocalDateTime.now(), null);
        Warehouse newWarehouse = new Warehouse("BU-001", "ZWOLLE-001", 60, 30, null, null);

        when(warehouseStore.findByBusinessUnitCode("BU-001")).thenReturn(existingWarehouse);

        replaceWarehouseUseCase.replace(newWarehouse);

        verify(warehouseStore, times(1)).update(newWarehouse);
        verify(archiveWarehouseOperation, times(1)).archive(existingWarehouse);
    }

    @Test
    @DisplayName("Should throw exception when warehouse does not exist")
    public void shouldThrowExceptionWhenWarehouseDoesNotExist() {
        Warehouse newWarehouse = new Warehouse("BU-001", "ZWOLLE-001", 60, 30, null, null);

        when(warehouseStore.findByBusinessUnitCode("BU-001")).thenReturn(null);

        assertThrows(WebApplicationException.class, () -> replaceWarehouseUseCase.replace(newWarehouse));
    }

    @Test
    @DisplayName("Should throw exception when warehouse is archived")
    public void shouldThrowExceptionWhenWarehouseIsArchived() {
        Warehouse existingWarehouse = new Warehouse("BU-001", "ZWOLLE-001", 50, 30, LocalDateTime.now(), LocalDateTime.now());
        Warehouse newWarehouse = new Warehouse("BU-001", "ZWOLLE-001", 60, 30, null, null);

        when(warehouseStore.findByBusinessUnitCode("BU-001")).thenReturn(existingWarehouse);

        assertThrows(WebApplicationException.class, () -> replaceWarehouseUseCase.replace(newWarehouse));
    }

    @Test
    @DisplayName("Should throw exception when new warehouse capacity is less than existing stock")
    public void shouldThrowExceptionWhenNewWarehouseCapacityIsLessThanExistingStock() {
        Warehouse existingWarehouse = new Warehouse("BU-001", "ZWOLLE-001", 50, 30, LocalDateTime.now(), null);
        Warehouse newWarehouse = new Warehouse("BU-001", "ZWOLLE-001", 20, 30, null, null);

        when(warehouseStore.findByBusinessUnitCode("BU-001")).thenReturn(existingWarehouse);

        assertThrows(WebApplicationException.class, () -> replaceWarehouseUseCase.replace(newWarehouse));
    }

    @Test
    @DisplayName("Should throw exception when new warehouse stock does not match existing stock")
    public void shouldThrowExceptionWhenNewWarehouseStockDoesNotMatchExistingStock() {
        Warehouse existingWarehouse = new Warehouse("BU-001", "ZWOLLE-001", 50, 30, LocalDateTime.now(), null);
        Warehouse newWarehouse = new Warehouse("BU-001", "ZWOLLE-001", 60, 20, null, null);

        when(warehouseStore.findByBusinessUnitCode("BU-001")).thenReturn(existingWarehouse);

        assertThrows(WebApplicationException.class, () -> replaceWarehouseUseCase.replace(newWarehouse));
    }

}
