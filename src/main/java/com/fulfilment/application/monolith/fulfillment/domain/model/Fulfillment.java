package com.fulfilment.application.monolith.fulfillment.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Fulfillment {
    private final Long storeId;
    private final Long productId;
    private final List<Long> warehouseIds;
}
