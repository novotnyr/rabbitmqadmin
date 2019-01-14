package com.github.novotnyr.rabbitmqadmin.command;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.Map;

public class PublishToExchangeRequest {
    @SerializedName("routing_key")
    private String routingKey;

    @SerializedName("payload")
    private String base64Payload;

    @SerializedName("payload_encoding")
    private String payloadEncoding = "base64";

    private Properties properties = new Properties();

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
        this.properties.setContentType(contentType);
    }

    public void setReplyTo(String replyTo) {
        if (replyTo == null) {
            return;
        }
        this.properties.setReplyTo(replyTo);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setHeaders(Map<String, String> headers) {
        this.properties.setHeaders(headers);
    }

    public static class Properties {
        @SerializedName("content_type")
        private String contentType;

        @SerializedName("reply_to")
        private String replyTo;

        private Map<String, String> headers = new LinkedHashMap<>();

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getReplyTo() {
            return replyTo;
        }

        public void setReplyTo(String replyTo) {
            this.replyTo = replyTo;
        }
    }
}
