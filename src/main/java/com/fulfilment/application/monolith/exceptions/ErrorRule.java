package com.fulfilment.application.monolith.exceptions;

import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static jakarta.ws.rs.core.Response.Status.fromStatusCode;

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
    //404
    WAREHOUSE_NOT_FOUND(Response.Status.NOT_FOUND,"Warehouse with the provided BusinessUnitCode not found", ErrorCode.WAREHOUSE_NOT_FOUND),
    WAREHOUSE_LOCATION_EXCEEDED_MAX_CAPACTY(Response.Status.CONFLICT,"Warehouse with provided location exceeded max capacity", ErrorCode.WAREHOUSE_LOCATION_EXCEEDED_MAX_CAPACTY);
    private final Response.Status httpStatus;
    private final String description;
    private final ErrorCode code;

    ErrorRule(Response.Status httpStatus, String description, ErrorCode code) {
        this.httpStatus = httpStatus;
        this.description = description;
        this.code = code;
    }


}
