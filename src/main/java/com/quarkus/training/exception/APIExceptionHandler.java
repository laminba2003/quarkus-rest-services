package com.quarkus.training.exception;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;
import static javax.ws.rs.core.Response.Status.*;

@Provider
public class APIExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof RequestException) {
            return handleRequestException((RequestException) exception);
        } else if (exception instanceof EntityNotFoundException) {
            return handleEntityNotFoundException((EntityNotFoundException) exception);
        }
        else if (exception instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) exception);
        }
        else {
            APIException apiException = new APIException("internal server error",
                    INTERNAL_SERVER_ERROR, LocalDateTime.now());
            return Response.status(apiException.getStatus())
                    .entity(exception).build();
        }
    }

    public Response handleRequestException(RequestException e) {
        APIException exception = new APIException(e.getMessage(),
                e.getStatus(), LocalDateTime.now());
        return Response.status(e.getStatus())
                .entity(exception).build();
    }

    public Response handleEntityNotFoundException(EntityNotFoundException e) {
        APIException exception = new APIException(e.getMessage(),
                NOT_FOUND, LocalDateTime.now());
        return Response.status(exception.getStatus())
                .entity(exception).build();
    }

    public Response handleConstraintViolationException(ConstraintViolationException e) {
        APIException exception = new APIException("invalid input",
                BAD_REQUEST, LocalDateTime.now());
        return Response.status(exception.getStatus())
                .entity(exception).build();
    }
}