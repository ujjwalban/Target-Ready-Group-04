package com.targetindia.EcomStreaming.exceptions;

public class InvalidCustomerId extends RuntimeException{
    public InvalidCustomerId(){

    }
    public InvalidCustomerId(String message){
        super(message);
    }
    public InvalidCustomerId(Throwable cause){
        super(cause);
    }
}
