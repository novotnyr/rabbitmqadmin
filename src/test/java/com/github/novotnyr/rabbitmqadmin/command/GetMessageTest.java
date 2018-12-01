package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import org.junit.Test;

import java.util.List;

public class GetMessageTest {
    @Test
    public void test() {
        RabbitConfiguration configuration = new RabbitConfiguration();
        configuration.setUser("guest");
        configuration.setPassword("guest");
        configuration.setVirtualHost("/");

        GetMessage command = new GetMessage(configuration);
        command.setQueue("cabbage");
        List<RetrievedMessage> messages = command.run();

        System.out.println(messages);

    }

    @Test
    public void testGetMessageFromUnknownQueue() {
        RabbitConfiguration configuration = new RabbitConfiguration();
        configuration.setUser("guest");
        configuration.setPassword("guest");
        configuration.setVirtualHost("/");
        configuration.setProtocol(RabbitConfiguration.Protocol.HTTP);
        configuration.setPort(15672);

        GetMessage command = new GetMessage(configuration);
        command.setQueue("UNKNOWNQUEUE");
        List<RetrievedMessage> messages = command.run();

        System.out.println(messages);

    }
}