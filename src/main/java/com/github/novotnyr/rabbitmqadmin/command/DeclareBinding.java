package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.github.novotnyr.rabbitmqadmin.util.UrlEncoder;
import com.google.gson.reflect.TypeToken;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class DeclareBinding extends AbstractRestCommand<Void> {
    private static final Type EMPTY = new TypeToken<Void>(){}.getType();

    public static final String URL_TEMPLATE = "/bindings/${vhost}/e/${exchange}/q/${queue}";;

    private String exchange;

    private String queue;

    private String routingKey;

    public DeclareBinding(RabbitConfiguration rabbitConfiguration) {
        super(rabbitConfiguration);
    }

    @Override
    protected Request buildRequest() {
        Map<String, Object> requestPayload = new LinkedHashMap<>();
        requestPayload.put("routing_key", this.routingKey);

        return new Request.Builder()
                .url(resolveUrl())
                .post(RequestBody.create(JSON, getGson().toJson(requestPayload)))
                .build();
    }

    @Override
    protected void onComplete() {
        logger.info("Declared binding {} ---[{}]---> {} @ {}", this.exchange, this.routingKey, this.queue, this.getVirtualHost());
    }

    @Override
    protected String getUrlSuffix() {
        String url = URL_TEMPLATE.replace("${vhost}", UrlEncoder.encode(this.getVirtualHost()));
        url = url.replace("${exchange}", UrlEncoder.encode(this.exchange));
        url = url.replace("${queue}", UrlEncoder.encode(this.queue));

        return url;
    }

    @Override
    protected Type getTypeToken() {
        return EMPTY;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
