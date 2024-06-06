package com.targetindia.EcomStreaming.exceptions;

public class CustomerIdException extends RuntimeException {
    public CustomerIdException(){
    }
    public CustomerIdException(String message){
        super(message);
    }
    public CustomerIdException(Throwable cause) {
        super(cause);
    }
}
