package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.github.novotnyr.rabbitmqadmin.util.UrlEncoder;
import com.google.gson.reflect.TypeToken;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PublishToExchange extends AbstractRestCommand<PublishToExchangeResponse> {
    private static final Type TYPE_TOKEN = new TypeToken<PublishToExchangeResponse>(){}.getType();

    private String exchange = "amq.default";

    private String routingKey;

    private String base64Contents;

    private String contentType;

    private String replyTo;

    public PublishToExchange(RabbitConfiguration rabbitConfiguration) {
        super(rabbitConfiguration);
    }

    @Override
    protected String getUrlSuffix() {
        return "/exchanges/" + UrlEncoder.encode(getVirtualHost()) + "/" + this.exchange + "/publish";
    }

    protected Request buildRequest() {
        return new Request.Builder()
                .url(resolveUrl())
                .post(getRequestBody())
                .build();
    }

    private RequestBody getRequestBody() {
        PublishToExchangeRequest request = new PublishToExchangeRequest();
        request.setRoutingKey(this.routingKey);
        request.setBase64Payload(this.base64Contents);
        request.setContentType(this.contentType);
        request.setReplyTo(this.replyTo);

        String jsonRequest = getGson().toJson(request);

        return RequestBody.create(JSON, jsonRequest);
    }

    @Override
    protected void handleFailedResponse(Response response, String responseBodyString) {
        if (response.code() == 404) {
            throw new ExchangeOrVirtualHostNotFoundException(this.getVirtualHost(), this.exchange, responseBodyString);
        }
        super.handleFailedResponse(response, responseBodyString);
    }

    @Override
    protected Type getTypeToken() {
        return TYPE_TOKEN;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public void setBase64Contents(String base64Contents) {
        this.base64Contents = base64Contents;
    }

    public void encodeAndSetUtf8Contents(String plainContents) {
        String base64 = Base64.getEncoder().encodeToString(plainContents.getBytes(StandardCharsets.UTF_8));
        setBase64Contents(base64);
    }

    public void encodeAndSetUtf8Contents(byte[] plainContents) {
        String base64 = Base64.getEncoder().encodeToString(plainContents);
        setBase64Contents(base64);
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
}
