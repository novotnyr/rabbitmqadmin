package com.github.novotnyr.rabbitmqadmin;

public class RabbitMqConnectionException extends RabbitmqAdminException {
    public RabbitMqConnectionException(RabbitConfiguration rabbitConfiguration, Throwable cause) {
        this("Unable to connect to RabbitMQ broker on " + rabbitConfiguration.getHost() + ":" + rabbitConfiguration.getPort() + " via " + rabbitConfiguration.getProtocol(), cause, rabbitConfiguration);
    }

    public RabbitMqConnectionException(Throwable cause) {
        this("Unable to connect to RabbitMQ broker", cause);
    }

    public RabbitMqConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RabbitMqConnectionException(String message, Throwable cause, RabbitConfiguration rabbitConfiguration) {
        super(message, cause);
        setRabbitConfiguration(rabbitConfiguration);
    }
}
