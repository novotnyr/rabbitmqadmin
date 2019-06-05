package com.github.novotnyr.rabbitmqadmin.command.script;

import com.github.novotnyr.rabbitmqadmin.command.Command;
import com.github.novotnyr.rabbitmqadmin.log.StdErr;

public class StdErrOutputSerializer implements ScriptOutputSerializer<Object> {
    private final StdErr stdErr;

    public StdErrOutputSerializer(StdErr stdErr) {
        this.stdErr = stdErr;
    }

    @Override
    public void serialize(Command<?> command, Object value) {
        stdErr.println(value.toString());
    }
}
