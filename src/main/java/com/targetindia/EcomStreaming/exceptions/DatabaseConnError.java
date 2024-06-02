package com.targetindia.EcomStreaming.exceptions;

public class DatabaseConnError extends RuntimeException{
    public DatabaseConnError(){

    }
    public DatabaseConnError(String message){
        super(message);
    }
    public DatabaseConnError(Throwable cause){
        super(cause);
    }
}
