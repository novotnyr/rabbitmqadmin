package com.github.novotnyr.rabbitmqadmin;

import static com.rabbitmq.client.ConnectionFactory.DEFAULT_AMQP_PORT;
import static com.rabbitmq.client.ConnectionFactory.DEFAULT_HOST;
import static com.rabbitmq.client.ConnectionFactory.DEFAULT_PASS;
import static com.rabbitmq.client.ConnectionFactory.DEFAULT_USER;

public class RabbitConfiguration {
    private String host = DEFAULT_HOST;

    private int port = DEFAULT_AMQP_PORT;

    private String virtualHost = "/";

    private String user = DEFAULT_USER;

    private String password = DEFAULT_PASS;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Rabbit Configuration: ");
        sb.append("host: ").append(host);
        sb.append(", port: ").append(port);
        sb.append(", virtualHost: ").append(virtualHost);
        sb.append(", user: ").append(user);
        sb.append(", password: ").append(password.replaceAll(".", "*"));
        return sb.toString();
    }
}
