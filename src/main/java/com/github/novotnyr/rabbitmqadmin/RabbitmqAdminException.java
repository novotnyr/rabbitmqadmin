package com.github.novotnyr.rabbitmqadmin;

public class RabbitmqAdminException extends RuntimeException {

    public RabbitmqAdminException() {
        super();
    }

    public RabbitmqAdminException(String msg) {
        super(msg);
    }

    public RabbitmqAdminException(String message, Throwable cause) {
        super(message, cause);
    }

    public RabbitmqAdminException(Throwable cause) {
        super(cause);
    }
}