package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

public abstract class AbstractRabbitmqAdminCommand {
    public final Logger logger = LoggerFactory.getLogger(getClass());

    private RabbitConfiguration configuration = new RabbitConfiguration();

    public void run() {
        logger.debug("Configuring ConnectionFactory from {}", configuration);

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(configuration.getHost());
        connectionFactory.setPort(configuration.getPort());
        connectionFactory.setVirtualHost(configuration.getVirtualHost());
        connectionFactory.setUsername(configuration.getUser());
        connectionFactory.setPassword(configuration.getPassword());

        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        doWithAdmin(admin);
    }

    protected abstract void doWithAdmin(RabbitAdmin admin);

    public void setConfiguration(RabbitConfiguration configuration) {
        this.configuration = configuration;
    }

    protected RabbitConfiguration getConfiguration() {
        return configuration;
    }
}
