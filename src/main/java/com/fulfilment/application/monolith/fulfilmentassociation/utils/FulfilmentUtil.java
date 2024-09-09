package com.fulfilment.application.monolith.fulfilmentassociation.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FulfilmentUtil {
    public  static Set<Long> getUniqueIds(List<Long> ids) {
        Set<Long> uniqueWarehouseIds = new HashSet<>(ids);
        return uniqueWarehouseIds;
    }
}
