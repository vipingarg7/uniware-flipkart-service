package com.uniware.integrations.web.exception;

import com.uniware.integrations.core.dto.api.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static Logger LOGGER = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

    @ExceptionHandler(BadRequest.class)
    public final ResponseEntity<Response> badRequest(BadRequest ex, WebRequest request) {
        Response exceptionResponse = new Response.Builder().message(ex.getMessage()).status("failure").build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
    }

    @ExceptionHandler(FailureResponse.class)
    public final ResponseEntity<Response> handleFailureResponse(FailureResponse ex, WebRequest request) {
        LOGGER.info("FailureResponse message :-  {}", ex.getMessage());
        Response response = new Response.Builder().status("failure").message(ex.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(AuthorizationServiceException.class)
    public final ResponseEntity<Response> authorizationServiceException(AuthorizationServiceException ex, WebRequest request) {
        Response response = new Response.Builder().status("failure").message(ex.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response> handleAllExceptions(Exception ex, WebRequest request) {
        LOGGER.info("Something went wrong :-  {}", ex.getMessage(), ex);
        Response exceptionResponse = new Response.Builder().message(ex.getMessage()).status("failure").build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
    }
}