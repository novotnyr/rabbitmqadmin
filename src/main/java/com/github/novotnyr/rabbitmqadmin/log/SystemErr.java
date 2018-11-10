package com.github.novotnyr.rabbitmqadmin.log;

public class SystemErr implements StdErr {
    @Override
    public void println(String message) {
        System.err.println(message);
    }
}
