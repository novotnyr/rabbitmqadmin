package com.github.novotnyr.rabbitmqadmin.command.script;

import com.github.novotnyr.rabbitmqadmin.command.Command;

public interface ScriptOutputSerializer<C extends Command<?>, T> {
    void serialize(C command, T value);
}
