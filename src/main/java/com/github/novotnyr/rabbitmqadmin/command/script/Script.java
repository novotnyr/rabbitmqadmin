package com.github.novotnyr.rabbitmqadmin.command.script;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.github.novotnyr.rabbitmqadmin.command.Command;

import java.util.ArrayList;
import java.util.List;

public class Script {
    private RabbitConfiguration configuration;

    private List<Command<?>> commands = new ArrayList<>();

    public List<Command<?>> getCommands() {
        return commands;
    }

    public void append(Command<?> command) {
        this.commands.add(command);
    }

    public void setConfiguration(RabbitConfiguration configuration) {
        this.configuration = configuration;
    }

    public RabbitConfiguration getConfiguration() {
        return configuration;
    }
}
