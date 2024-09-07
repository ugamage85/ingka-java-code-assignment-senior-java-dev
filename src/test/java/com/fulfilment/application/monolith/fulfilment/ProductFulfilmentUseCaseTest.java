package com.fulfilment.application.monolith.fulfilment;

import com.fulfilment.application.monolith.fulfillment.adaptors.database.DbFulfillmentUnit;
import com.fulfilment.application.monolith.fulfillment.adaptors.database.FulfillmentUnitRepository;
import com.fulfilment.application.monolith.fulfillment.domain.usecases.ProductFulfilmentUseCase;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ProductFulfilmentUseCaseTest {
    private ProductFulfilmentUseCase productFulfilmentUseCase;
    private FulfillmentUnitRepository fulfillmentUnitRepository;

    @BeforeEach
    public void setup() {
        fulfillmentUnitRepository = Mockito.mock(FulfillmentUnitRepository.class);
        productFulfilmentUseCase = new ProductFulfilmentUseCase(fulfillmentUnitRepository);
    }

    @Test
    public void testCreateFulfillmentUnit_Success() {
        DbFulfillmentUnit fulfillmentUnit = new DbFulfillmentUnit();
        when(fulfillmentUnitRepository.list(anyString(), (Object) any(), (Object) any())).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> productFulfilmentUseCase.createFulfillmentUnit(fulfillmentUnit));
    }

    @Test
    public void testCreateFulfillmentUnit_MaxProductPerStore() {
        DbFulfillmentUnit fulfillmentUnit = new DbFulfillmentUnit();
        when(fulfillmentUnitRepository.list("product = ?1 and store = ?2", fulfillmentUnit.getProduct(), fulfillmentUnit.getStore()))
                .thenReturn(Collections.nCopies(2, new DbFulfillmentUnit()));

        assertThrows(WebApplicationException.class, () -> productFulfilmentUseCase.createFulfillmentUnit(fulfillmentUnit));
    }

    @Test
    public void testCreateFulfillmentUnit_MaxStorePerWarehouse() {
        DbFulfillmentUnit fulfillmentUnit = new DbFulfillmentUnit();
        when(fulfillmentUnitRepository.list("store = ?1", fulfillmentUnit.getStore()))
                .thenReturn(Collections.nCopies(3, new DbFulfillmentUnit()));

        assertThrows(WebApplicationException.class, () -> productFulfilmentUseCase.createFulfillmentUnit(fulfillmentUnit));
    }

    @Test
    public void testCreateFulfillmentUnit_MaxProductPerWarehouse() {
        DbFulfillmentUnit fulfillmentUnit = new DbFulfillmentUnit();
        when(fulfillmentUnitRepository.list("warehouse = ?1", fulfillmentUnit.getWarehouse()))
                .thenReturn(Collections.nCopies(5, new DbFulfillmentUnit()));

        assertThrows(WebApplicationException.class, () -> productFulfilmentUseCase.createFulfillmentUnit(fulfillmentUnit));
    }
}
