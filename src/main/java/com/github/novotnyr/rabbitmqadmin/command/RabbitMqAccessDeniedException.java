package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitmqAdminException;

public class RabbitMqAccessDeniedException extends RabbitmqAdminException {
    public RabbitMqAccessDeniedException() {
    }

    public RabbitMqAccessDeniedException(String msg) {
        super(msg);
    }

    public RabbitMqAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RabbitMqAccessDeniedException(Throwable cause) {
        super(cause);
    }
}
