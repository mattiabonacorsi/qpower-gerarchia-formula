package org.example.utils;

public class TracingException extends RuntimeException {
    public TracingException(String message) {
        super(message);
    }

    public TracingException(String message, Throwable cause) {
        super(message, cause);
    }
} 