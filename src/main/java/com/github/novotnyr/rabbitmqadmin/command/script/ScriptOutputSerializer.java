package com.github.novotnyr.rabbitmqadmin.command.script;

public interface ScriptOutputSerializer<T> {
    void serialize(Class<?> commandClass, T value);
}
