package com.fulfilment.application.monolith.exceptions;

import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorRule {
    //400
    FIELD_IS_REQUIRED(Response.Status.BAD_REQUEST,"Validation error", ErrorCode.FIELD_IS_REQUIRED),
    BODY_MISSING(Response.Status.BAD_REQUEST,"Request body is missing", ErrorCode.BODY_MISSING);

    private final Response.Status httpStatus;
    private final String description;
    private final ErrorCode code;

}
