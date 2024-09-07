package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ArchiveWarehouseUseCaseTest {
    @Mock
    private WarehouseStore warehouseStore;

    @InjectMocks
    private ArchiveWarehouseUseCase archiveWarehouseUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should archive warehouse when warehouse exists")
    public void shouldArchiveWarehouse() {
        Warehouse warehouse = new Warehouse("BU-001", "ZWOLLE-001", 50, 30, null, null);

        archiveWarehouseUseCase.archive(warehouse);

        verify(warehouseStore, times(1)).remove(warehouse);
    }

    @Test
    @DisplayName("Should throw exception when warehouse is null")
    public void shouldThrowExceptionWhenWarehouseIsNull() {
        assertThrows(IllegalArgumentException.class, () -> archiveWarehouseUseCase.archive(null));
    }
}
