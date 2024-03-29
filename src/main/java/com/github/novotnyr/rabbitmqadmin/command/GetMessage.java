package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.github.novotnyr.rabbitmqadmin.util.UrlEncoder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class GetMessage extends AbstractScriptableCommand<List<RetrievedMessage>> {
    private static final Type TYPE_TOKEN = new TypeToken<Collection<RetrievedMessage>>(){}.getType();

    private String queue;

    public GetMessage(RabbitConfiguration rabbitConfiguration) {
        super(rabbitConfiguration);
    }

    @Override
    protected String getUrlSuffix() {
        return "/queues/" + UrlEncoder.encode(getVirtualHost()) + "/" + this.queue +  "/get";
    }

    protected Request buildRequest() {
        return new Request.Builder()
                .url(resolveUrl())
                .post(getRequestBody())
                .build();
    }

    private RequestBody getRequestBody() {
        JsonObject payload = new JsonObject();
        payload.addProperty("count", 1);
        payload.addProperty("encoding", GetMessageRequest.Encoding.AUTO.getCode());
        // Rabbit 3.6.x compatibility
        payload.addProperty("requeue", "false");
        // Rabbit 3.7.x compatibility
        payload.addProperty("ackmode", "ack_requeue_false");

        String jsonRequest = getGson().toJson(payload);
        return RequestBody.create(JSON, jsonRequest);
    }

    @Override
    protected void handleFailedResponse(Response response, String responseBodyString) {
        if (response.code() == 404) {
            throw new QueueOrVirtualHostNotFoundException(this.getVirtualHost(), this.queue, responseBodyString);
        }
        super.handleFailedResponse(response, responseBodyString);
    }

    @Override
    protected Type getTypeToken() {
        return TYPE_TOKEN;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
