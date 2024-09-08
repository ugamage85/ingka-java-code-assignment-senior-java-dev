package com.fulfilment.application.monolith.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;

@Provider
@RequiredArgsConstructor
public class ErrorMapper implements ExceptionMapper<WarehouseException> {
private final ObjectMapper objectMapper;

    @Override
    public Response toResponse(WarehouseException exception) {
        int code = 500;
        String errorCode =  "INTERNAL_SERVER_ERROR";
        if (exception != null) {
            code = ((WarehouseException) exception).getRule().getHttpStatus().getStatusCode();
            errorCode = ((WarehouseException) exception).getRule().getCode().name();
        }
        
        ObjectNode exceptionJson = objectMapper.createObjectNode();
        //exceptionJson.put("exceptionType", exception.getRule().getDescription());
        exceptionJson.put("code", code);
        exceptionJson.put("errorCode", errorCode);
        exceptionJson.put("message", exception.getMessage());

        return Response.status(code).entity(exceptionJson).build();
    }
}
