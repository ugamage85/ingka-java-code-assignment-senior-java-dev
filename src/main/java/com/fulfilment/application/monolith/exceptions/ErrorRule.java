package com.fulfilment.application.monolith.exceptions;

import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static jakarta.ws.rs.core.Response.Status.fromStatusCode;

@Getter
@RequiredArgsConstructor
public enum ErrorRule {
    //400
    FIELD_IS_REQUIRED(Response.Status.BAD_REQUEST,"Validation error", ErrorCode.FIELD_IS_REQUIRED),
    BODY_MISSING(Response.Status.BAD_REQUEST,"Request body is missing", ErrorCode.BODY_MISSING),

    //422
    WAREHOUSE_ALREADY_ARCHIVED(fromStatusCode(422),"Warehouse with the provided BusinessUnitCode already archived", ErrorCode.WAREHOUSE_ALREADY_ARCHIVED),
    WAREHOUSE_CAPACITY_NOT_ENOUGH(fromStatusCode(422),"New warehouse's capacity cannot accommodate the stock from the warehouse being replaced", ErrorCode.WAREHOUSE_CAPACITY_NOT_ENOUGH),
    WAREHOUSE_STOCK_MISMATCH(fromStatusCode(422),"Stock of the new warehouse does not match the stock of the previous warehouse", ErrorCode.WAREHOUSE_STOCK_MISMATCH),

    //404
    WAREHOUSE_NOT_FOUND(Response.Status.NOT_FOUND,"Warehouse with the provided BusinessUnitCode not found", ErrorCode.WAREHOUSE_NOT_FOUND);

    private final Response.Status httpStatus;
    private final String description;
    private final ErrorCode code;

}
