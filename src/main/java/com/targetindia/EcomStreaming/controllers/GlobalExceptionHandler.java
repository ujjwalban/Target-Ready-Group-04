package com.targetindia.EcomStreaming.controllers;

import com.targetindia.EcomStreaming.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(CustomerIdException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public static String customerIdException(CustomerIdException e){
    return e.getMessage();
  }

  @ExceptionHandler(ProductIdException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public static String productIdException(ProductIdException e){
    return e.getMessage();
  }

  @ExceptionHandler(ProductQuantityException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public static String productQuantityException(ProductQuantityException e){
    return e.getMessage();
  }

  @ExceptionHandler(CustomerNotFoundException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public static String customerNotFoundException(CustomerNotFoundException e){
    return e.getMessage();
  }

  @ExceptionHandler(ProductNotFoundException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public static String productNotFoundException(ProductNotFoundException e){
    return e.getMessage();
  }
}
