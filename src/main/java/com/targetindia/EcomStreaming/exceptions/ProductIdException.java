package com.targetindia.EcomStreaming.exceptions;

public class ProductIdException extends RuntimeException {
    public ProductIdException() {
    }
    public ProductIdException(String message){
        super(message);
    }
    public ProductIdException(Throwable cause){
        super(cause);
    }

}
