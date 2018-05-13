package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class WhoAmI extends AbstractRestCommand<String> {
    private static final Type TYPE_TOKEN = new TypeToken<String>(){}.getType();

    public WhoAmI(RabbitConfiguration rabbitConfiguration) {
        super(rabbitConfiguration);
    }

    @Override
    protected String getUrlSuffix() {
        return "/whoami";
    }

    @Override
    protected Type getTypeToken() {
        return TYPE_TOKEN;
    }
}
