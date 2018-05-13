package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.Queue;
import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class ListQueues extends AbstractRestCommand<List<Queue>> {
    private static final Type LIST_OF_QUEUES_TYPE = new TypeToken<Collection<Queue>>(){}.getType();

    public ListQueues(RabbitConfiguration rabbitConfiguration) {
        super(rabbitConfiguration);
    }

    @Override
    protected String getUrlSuffix() {
        return "/queues";
    }

    @Override
    protected Type getTypeToken() {
        return LIST_OF_QUEUES_TYPE;
    }
}
