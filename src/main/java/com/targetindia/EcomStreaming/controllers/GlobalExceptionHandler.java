package com.targetindia.EcomStreaming.controllers;

import com.targetindia.EcomStreaming.exceptions.CustomerIdException;
import com.targetindia.EcomStreaming.exceptions.ProductIdException;
import com.targetindia.EcomStreaming.exceptions.ProductQuantityException;
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

}
