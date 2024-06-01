package com.targetindia.EcomStreaming.exceptions;

public class InvalidProductId extends RuntimeException{
    public InvalidProductId(){

    }
    public InvalidProductId(String message){
        super(message);
    }
    public InvalidProductId(Throwable cause){
        super(cause);
    }

}
