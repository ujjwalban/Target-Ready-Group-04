package com.targetindia.EcomStreaming.exceptions;

public class StockLevelException extends RuntimeException {
    public StockLevelException() {
    }

    public StockLevelException(String message) {
        super(message);
    }

    public StockLevelException(Throwable cause) {
        super(cause);
    }
}
