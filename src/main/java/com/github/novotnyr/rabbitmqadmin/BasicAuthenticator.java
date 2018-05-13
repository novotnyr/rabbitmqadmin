package com.github.novotnyr.rabbitmqadmin;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import java.io.IOException;

public class BasicAuthenticator implements Authenticator {
    private String user;

    private String password;

    public BasicAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (response.request().header("Authorization") != null) {
            return null; // Give up, we've already attempted to authenticate.
        }

        String credential = Credentials.basic(this.user, this.password);
        return response
                .request().newBuilder()
                    .header("Authorization", credential)
                    .build();
    }

}
