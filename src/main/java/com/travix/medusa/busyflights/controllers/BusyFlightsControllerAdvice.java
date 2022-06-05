package com.travix.medusa.busyflights.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

import javax.validation.ValidationException;

@ControllerAdvice
public class BusyFlightsControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String validationExceptionHandler(Exception exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String restClientExceptionHandler(Exception exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String internalServerErrorHandler(Exception exception) {
        return exception.getMessage();
    }
}
