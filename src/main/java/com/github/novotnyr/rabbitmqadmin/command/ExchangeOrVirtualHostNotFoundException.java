package com.github.novotnyr.rabbitmqadmin.command;

public class ExchangeOrVirtualHostNotFoundException extends RuntimeException {

    private final String virtualHost;
    private final String exchange;
    private final String responseBodyString;

    public ExchangeOrVirtualHostNotFoundException(String virtualHost, String exchange, String responseBodyString) {
        super("Exchange (" + exchange + ") or virtual host (" + virtualHost + ") not found");
        this.virtualHost = virtualHost;
        this.exchange = exchange;
        this.responseBodyString = responseBodyString;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public String getExchange() {
        return exchange;
    }

    public String getResponseBodyString() {
        return responseBodyString;
    }
}