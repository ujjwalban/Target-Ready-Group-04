package com.targetindia.EcomStreaming.exceptions;

import com.targetindia.EcomStreaming.controllers.GlobalExceptionHandler;

public class ProductQuantityException extends RuntimeException {
    public ProductQuantityException(){
    }
    public ProductQuantityException(String message) {
        super(message);
    }
    public ProductQuantityException(Throwable cause){
        super(cause);
    }
}
