package com.github.novotnyr.rabbitmqadmin.command;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.Map;

public class RetrievedMessage {
    @SerializedName("payload_bytes")
    private String payloadSize;

    private boolean redelivered;

    private String exchange;

    @SerializedName("message_count")
    private String messagesLeftInQueue;

    private Properties properties;

    private String payload;

    public String getPayloadSize() {
        return payloadSize;
    }

    public void setPayloadSize(String payloadSize) {
        this.payloadSize = payloadSize;
    }

    public boolean isRedelivered() {
        return redelivered;
    }

    public void setRedelivered(boolean redelivered) {
        this.redelivered = redelivered;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getMessagesLeftInQueue() {
        return messagesLeftInQueue;
    }

    public void setMessagesLeftInQueue(String messagesLeftInQueue) {
        this.messagesLeftInQueue = messagesLeftInQueue;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RetrievedMessage{");
        sb.append("payloadSize='").append(payloadSize).append('\'');
        sb.append(", redelivered=").append(redelivered);
        sb.append(", exchange='").append(exchange).append('\'');
        sb.append(", messagesLeftInQueue='").append(messagesLeftInQueue).append('\'');
        sb.append(", payload='").append(payload).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class Properties {
        @SerializedName("delivery_mode")
        private int deliveryMode;

        private Map<String, String> headers = new LinkedHashMap<>();

        public int getDeliveryMode() {
            return deliveryMode;
        }

        public void setDeliveryMode(int deliveryMode) {
            this.deliveryMode = deliveryMode;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }
    }
}
