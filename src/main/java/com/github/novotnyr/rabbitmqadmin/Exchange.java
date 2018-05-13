package com.github.novotnyr.rabbitmqadmin;

public class Exchange {
    private String name;

    private String vhost;

    private boolean durable;

    private boolean autoDelete;

    private boolean internal;

    private Object arguments;

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

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public Object getArguments() {
        return arguments;
    }

    public void setArguments(Object arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Exchange{");
        sb.append("name='").append(name).append('\'');
        sb.append(", vhost='").append(vhost).append('\'');
        sb.append(", durable=").append(durable);
        sb.append(", autoDelete=").append(autoDelete);
        sb.append(", internal=").append(internal);
        sb.append(", arguments=").append(arguments);
        sb.append('}');
        return sb.toString();
    }
}
