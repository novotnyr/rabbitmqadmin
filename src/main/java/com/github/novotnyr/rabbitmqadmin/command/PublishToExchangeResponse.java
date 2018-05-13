package com.github.novotnyr.rabbitmqadmin.command;

public class PublishToExchangeResponse {
    private boolean routed;

    public boolean isRouted() {
        return routed;
    }

    public void setRouted(boolean routed) {
        this.routed = routed;
    }
}
