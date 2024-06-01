package com.targetindia.EcomStreaming.exceptions;

public class InvalidProductQuantity extends RuntimeException{
    public InvalidProductQuantity(){

    }
    public InvalidProductQuantity(String message){
        super(message);
    }
    public InvalidProductQuantity(Throwable cause){
        super(cause);
    }
}
