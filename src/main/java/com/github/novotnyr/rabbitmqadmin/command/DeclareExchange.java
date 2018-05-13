package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitmqAdminException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

public class DeclareExchange extends AbstractRabbitmqAdminCommand {
    private String name;

    private String type;

    private boolean durable;

    private boolean autoDelete;

    @Override
    protected void doWithAdmin(RabbitAdmin admin) {
        org.springframework.amqp.core.Exchange exchange = null;
        switch(this.type) {
            case "direct":
                exchange = new DirectExchange(name, durable, autoDelete);
                break;
            case "fanout":
                exchange = new FanoutExchange(name, durable, autoDelete);
                break;
            case "headers":
                exchange = new HeadersExchange(name, durable, autoDelete);
                break;
            case "topic":
                exchange = new TopicExchange(name, durable, autoDelete);
                break;
            default:
                throw new RabbitmqAdminException("Unknown exchange type [" + this.type + "]");
        }

        admin.declareExchange(exchange);
        logger.info("Declared exchange " + exchange);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }
}
