package com.targetindia.EcomStreaming.controllers;

import com.targetindia.EcomStreaming.exceptions.InvalidCustomerId;
import com.targetindia.EcomStreaming.exceptions.InvalidProductId;
import com.targetindia.EcomStreaming.exceptions.InvalidProductQuantity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class GlobalExceptionHandler {
  @ExceptionHandler(InvalidCustomerId.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public String customerIdException(InvalidCustomerId e){
    return e.getMessage();
  }

  @ExceptionHandler(InvalidProductId.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public String productIdException(InvalidProductId e){
    return e.getMessage();
  }

  @ExceptionHandler(InvalidProductQuantity.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public String productQuantityException(InvalidProductQuantity e){
    return e.getMessage();
  }
}
