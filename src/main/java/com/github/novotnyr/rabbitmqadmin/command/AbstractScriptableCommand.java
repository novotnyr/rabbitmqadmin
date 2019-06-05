package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;

public abstract class AbstractScriptableCommand<T> extends AbstractRestCommand<T>  {
    private String description;

    public AbstractScriptableCommand(RabbitConfiguration rabbitConfiguration) {
        super(rabbitConfiguration);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
