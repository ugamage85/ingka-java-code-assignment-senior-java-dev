package com.fulfilment.application.monolith.exceptions;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
//@RequiredArgsConstructor
public enum ErrorRule {
    //400
    FIELD_IS_REQUIRED(Response.Status.BAD_REQUEST,"Validation error", ErrorCode.FIELD_IS_REQUIRED),
    BODY_MISSING(Response.Status.BAD_REQUEST,"Request body is missing", ErrorCode.BODY_MISSING),

    //422
    WAREHOUSE_ALREADY_ARCHIVED(Response.Status.CONFLICT,"Warehouse with the provided BusinessUnitCode already archived", ErrorCode.WAREHOUSE_ALREADY_ARCHIVED),
    WAREHOUSE_ALREADY_EXIST(Response.Status.CONFLICT,"Warehouse with the provided BusinessUnitCode already exist", ErrorCode.WAREHOUSE_ALREADY_EXIST),
    WAREHOUSE_CAPACITY_NOT_ENOUGH(Response.Status.CONFLICT,"New warehouse's capacity cannot accommodate the stock from the warehouse being replaced", ErrorCode.WAREHOUSE_CAPACITY_NOT_ENOUGH),
    WAREHOUSE_STOCK_MISMATCH(Response.Status.CONFLICT,"Stock of the new warehouse does not match the stock of the previous warehouse", ErrorCode.WAREHOUSE_STOCK_MISMATCH),
    MAX_WAREHOUSE_NUMBER_REACHED(Response.Status.CONFLICT,"Maximum number of warehouses reached", ErrorCode.MAX_WAREHOUSE_NUMBER_REACHED),
    BUSINESS_UNIT_CODE_NOT_MATCH(Response.Status.CONFLICT,"BusinessUnitCode not match", ErrorCode.BUSINESS_UNIT_CODE_NOT_MATCH),
    WAREHOUSE_LOCATION_EXCEEDED_MAX_CAPACTY(Response.Status.CONFLICT,"Warehouse with provided location exceeded max capacity", ErrorCode.WAREHOUSE_LOCATION_EXCEEDED_MAX_CAPACTY),

    FULFILLMENT_ALREADY_EXIST(Response.Status.CONFLICT,"Fulfillment with the provided storeId, productId, warehouses already exist", ErrorCode.FULFILLMENT_ALREADY_EXIST),
    PRODUCT_FULFILMENT_MAX_WAREHOUSES_PER_STORE_EXCEEDED(Response.Status.CONFLICT,"A product can be fulfilled by up to 2 warehouses per store", ErrorCode.PRODUCT_FULFILMENT_MAX_WAREHOUSES_PER_STORE_EXCEEDED)
    ,STORE_FULFILMENT_MAX_WAREHOUSES_EXCEEDED(Response.Status.CONFLICT,"A store can be fulfilled by up to 3 warehouses", ErrorCode.STORE_FULFILMENT_MAX_WAREHOUSES_EXCEEDED),
    WAREHOUSE_MAX_PRODUCT_TYPES_EXCEEDED(Response.Status.CONFLICT,"Warehouse can store up to 5 types of products", ErrorCode.WAREHOUSE_MAX_PRODUCT_TYPES_EXCEEDED),

    //404
    WAREHOUSE_NOT_FOUND(Response.Status.NOT_FOUND,"Warehouse with the provided BusinessUnitCode not found", ErrorCode.WAREHOUSE_NOT_FOUND),
    PRODUCT_NOT_FOUND(Response.Status.NOT_FOUND,"Product with the provided id not found", ErrorCode.PRODUCT_NOT_FOUND),
    STORE_NOT_FOUND(Response.Status.NOT_FOUND,"Store with the provided id not found", ErrorCode.STORE_NOT_FOUND);

    private final Response.Status httpStatus;
    private final String description;
    private final ErrorCode code;

    ErrorRule(Response.Status httpStatus, String description, ErrorCode code) {
        this.httpStatus = httpStatus;
        this.description = description;
        this.code = code;
    }


}
