package com.uniware.integrations.web.exception;

import com.unicommerce.platform.integration.ServiceResponse;
import com.unicommerce.platform.integration.Error;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);


    // Todo : should i replace the BadRequest and Failure Exception with ApiResponseException as in shopify
    @ExceptionHandler(BadRequest.class)
    public final ResponseEntity<ServiceResponse> badRequest(BadRequest ex) {
        ServiceResponse response = new ServiceResponse();
        response.setSuccessful(false);
        response.setErrors(Collections.singletonList(new Error("BAD_REQUEST", ex.getMessage())));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FailureResponse.class)
    public final ResponseEntity<ServiceResponse> handleFailureResponse(FailureResponse ex) {
        LOGGER.info("FailureResponse message :-  {}", ex.getMessage());
        ServiceResponse response = new ServiceResponse();
        response.setSuccessful(false);
        response.setErrors(Collections.singletonList(new Error("FAILURE", ex.getMessage())));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ServiceResponse> handleAllExceptions(Exception ex) {
        LOGGER.info("Something went wrong :-  {}", ex.getMessage(), ex);
        LOGGER.info("FailureResponse message :-  {}", ex.getMessage());
        ServiceResponse response = new ServiceResponse();
        response.setSuccessful(false);
        response.setErrors(Collections.singletonList(new Error("FAILURE", ex.getMessage())));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}