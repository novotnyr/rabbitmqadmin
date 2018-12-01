package com.github.novotnyr.rabbitmqadmin.command;

public class QueueOrVirtualHostNotFoundException extends RuntimeException {

    private final String virtualHost;
    private final String queue;
    private final String responseBodyString;

    public QueueOrVirtualHostNotFoundException(String virtualHost, String queue, String responseBodyString) {
        super("Queue (" + queue + ") or virtual host (" + virtualHost + ") not found");
        this.virtualHost = virtualHost;
        this.queue = queue;
        this.responseBodyString = responseBodyString;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public String getQueue() {
        return queue;
    }

    public String getResponseBodyString() {
        return responseBodyString;
    }
}