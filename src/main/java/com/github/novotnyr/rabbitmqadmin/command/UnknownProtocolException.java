package com.github.novotnyr.rabbitmqadmin.command;

public class UnknownProtocolException extends RuntimeException {

    public UnknownProtocolException() {
        super();
    }

    public UnknownProtocolException(String msg) {
        super(msg);
    }

    public UnknownProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownProtocolException(Throwable cause) {
        super(cause);
    }
}