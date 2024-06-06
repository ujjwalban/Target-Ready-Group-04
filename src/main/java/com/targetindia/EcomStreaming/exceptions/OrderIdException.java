package com.targetindia.EcomStreaming.exceptions;

public class OrderIdException extends RuntimeException {
    public OrderIdException() {
    }

    public OrderIdException(String message) {
        super(message);
    }

    public OrderIdException(Throwable cause) {
        super(cause);
    }
}
