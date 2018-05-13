package com.github.novotnyr.rabbitmqadmin;

public class Queue {
    private String name;

    private String vhost;

    private boolean durable;

    private boolean autoDelete;

    private boolean exclusive;

    private Object arguments;

    private String node;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public Object getArguments() {
        return arguments;
    }

    public void setArguments(Object arguments) {
        this.arguments = arguments;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Queue{");
        sb.append("name='").append(name).append('\'');
        sb.append(", vhost='").append(vhost).append('\'');
        sb.append(", durable=").append(durable);
        sb.append(", autoDelete=").append(autoDelete);
        sb.append(", exclusive=").append(exclusive);
        sb.append(", arguments=").append(arguments);
        sb.append(", node='").append(node).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
