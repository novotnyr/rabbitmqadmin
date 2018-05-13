package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.Exchange;
import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;


public class ListExchanges extends AbstractRestCommand<List<Exchange>> {
    private static final Type LIST_OF_EXCHANGES_TYPE = new TypeToken<Collection<Exchange>>(){}.getType();

    public ListExchanges(RabbitConfiguration rabbitConfiguration) {
        super(rabbitConfiguration);
    }

    @Override
    protected String getUrlSuffix() {
        return "/exchanges";
    }

    @Override
    protected Type getTypeToken() {
        return LIST_OF_EXCHANGES_TYPE;
    }

}
