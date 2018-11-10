package com.github.novotnyr.rabbitmqadmin.command;

import java.util.concurrent.Callable;

public interface Command<V> extends Callable<V> {
    V run();

    @Override
    default V call() throws Exception {
        return run();
    }
}
