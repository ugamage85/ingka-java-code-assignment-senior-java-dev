package com.fulfilment.application.monolith.fulfillment.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class FulfillmentRequest {
    private Long storeId;
    private Long productId;
    private List<Long> warehouseIds;
}
