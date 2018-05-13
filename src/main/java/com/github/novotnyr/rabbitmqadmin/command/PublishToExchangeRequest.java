package com.github.novotnyr.rabbitmqadmin.command;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.Map;

public class PublishToExchangeRequest {
    private Map<String, Object> properties = new LinkedHashMap<>();

    @SerializedName("routing_key")
    private String routingKey;

    @SerializedName("payload")
    private String base64Payload;

    @SerializedName("payload_encoding")
    private String payloadEncoding = "base64";

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getBase64Payload() {
        return base64Payload;
    }

    public void setBase64Payload(String base64Payload) {
        this.base64Payload = base64Payload;
    }

    public String getPayloadEncoding() {
        return payloadEncoding;
    }

    public void setContentType(String contentType) {
        if (contentType == null) {
            return;
        }
        this.properties.put("content_type", contentType);
    }

    public void setReplyTo(String replyTo) {
        if (replyTo == null) {
            return;
        }
        this.properties.put("reply_to", replyTo);
    }
}
