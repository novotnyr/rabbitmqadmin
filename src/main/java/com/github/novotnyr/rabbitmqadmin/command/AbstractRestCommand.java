package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.BasicAuthenticator;
import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.github.novotnyr.rabbitmqadmin.RabbitMqConnectionException;
import com.github.novotnyr.rabbitmqadmin.RabbitmqAdminException;
import com.github.novotnyr.rabbitmqadmin.util.LoggingOkHttpInterceptor;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ConnectException;

public abstract class AbstractRestCommand<T> implements Command<T> {
    public final Logger logger = LoggerFactory.getLogger(getClass());

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Gson gson;

    private RabbitConfiguration rabbitConfiguration;

    public AbstractRestCommand(RabbitConfiguration rabbitConfiguration) {
        this.rabbitConfiguration = rabbitConfiguration;
        this.gson = buildGson();
    }

    protected Gson buildGson() {
        return new Gson();
    };

    public T run() {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .authenticator(new BasicAuthenticator(rabbitConfiguration.getUser(), rabbitConfiguration.getPassword()))
                    .followRedirects(false)
                    .addInterceptor(new LoggingOkHttpInterceptor())
                    .build();

            Request request = buildRequest();
            Response response = client.newCall(request).execute();
            try (ResponseBody responseBody = response.body()) {
                String responseBodyString = responseBody.string();
                if (!response.isSuccessful()) {
                    if (response.code() == 401) {
                        throw new RabbitMqAccessDeniedException("Failed to execute REST API call: Access denied");
                    } else {
                        handleFailedResponse(response, responseBodyString);
                    }
                }
                handleRawJson(responseBodyString);
                Object result = gson.fromJson(responseBodyString, getTypeToken());
                onComplete();
                return (T) result;
            }
        } catch (ConnectException e) {
            if (this.rabbitConfiguration == null) {
                throw new RabbitMqConnectionException(e);
            } else {
                throw new RabbitMqConnectionException(this.rabbitConfiguration, e);
            }
        } catch (IOException e) {
            throw new RabbitmqAdminException("Failed to execute REST API call", e);
        }
    }

    protected void handleFailedResponse(Response response, String responseBodyString) {
        throw new RabbitmqAdminException(("Command failed: " + responseBodyString));
    }


    protected void handleRawJson(String json) {
        if (json.contains("not_authorized")) {
            throw new RabbitmqAdminException("Failed to execute REST API call: Access denied");
        }
    }

    protected Request buildRequest() {
        return new Request.Builder()
                        .url(resolveUrl())
                        .get()
                        .build();
    }

    protected void onComplete() {
        // do nothing
    }

    protected abstract String getUrlSuffix();

    protected String resolveUrl() {
        return getBaseUrl().append(getUrlSuffix()).toString();
    }

    protected StringBuilder getBaseUrl() {
        return new StringBuilder()
                .append(getProtocol() + "://")
                .append(getRabbitConfiguration().getHost())
                .append(":")
                .append(getRabbitConfiguration().getPort())
                .append("/api");
    }

    protected String getProtocol() {
        return this.getRabbitConfiguration().getProtocol().name().toLowerCase();
    }

    protected abstract Type getTypeToken();

    protected Gson getGson() {
        return gson;
    }

    protected RabbitConfiguration getRabbitConfiguration() {
        return rabbitConfiguration;
    }

    protected String getVirtualHost() {
        return rabbitConfiguration.getVirtualHost();
    }
}
