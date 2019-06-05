package com.github.novotnyr.rabbitmqadmin.command.script;

import com.github.novotnyr.rabbitmqadmin.command.Command;

public interface ScriptOutputSerializer<T> {
    void serialize(Command<?> command, T value);
}
