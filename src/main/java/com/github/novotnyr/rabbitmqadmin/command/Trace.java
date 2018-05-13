package com.github.novotnyr.rabbitmqadmin.command;

import com.google.gson.Gson;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;


public class Trace extends AbstractRabbitmqAdminCommand {
    public static final String PUBLISH_ROUTING_KEY_PREFIX = "publish.";
    public static final String DELIVER_ROUTING_KEY_PREFIX = "deliver.";
    private Gson gson = new Gson();

    @Override
    protected void doWithAdmin(RabbitAdmin admin) {
        Queue loggerQueue = admin.declareQueue();
        admin.declareBinding(bindTraceExchange(loggerQueue));

        ConnectionFactory connectionFactory = admin.getRabbitTemplate().getConnectionFactory();
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        listenerContainer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                handleMessage(message);
            }
        });
        listenerContainer.setQueues(loggerQueue);
        listenerContainer.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                listenerContainer.stop();
            }
        });
        while(true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println("Stopping listener");
            }
        }
    }

    private void handleMessage(Message message) {
        String contentType = message.getMessageProperties().getContentType();
        if(contentType == null) {
            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            Map<String, Object> propertiesHeaders = (Map<String, Object>) headers.getOrDefault("properties", Collections.emptyMap());
            contentType = propertiesHeaders.getOrDefault("content_type", "utf-8").toString();
        }

        if("application/json".equals(contentType)) {
            handleJsonMessage(message);
        } else {
            System.out.println(message);
        }
    }

    private void handleJsonMessage(Message message) {
        byte[] body = message.getBody();
        String bodyString = new String(body, getCharset(message));
        log(message, bodyString);
    }

    private Charset getCharset(Message message) {
        String contentEncoding = message.getMessageProperties().getContentEncoding();
        if(contentEncoding == null) {
            contentEncoding = "utf-8";
        }
        return Charset.forName(contentEncoding);
    }

    public void log(Message message, String body) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String exchangeName = headers.getOrDefault("exchange_name", "?exchange?").toString();
        Object routingKeys = headers.get("routing_keys");
        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        if(receivedRoutingKey.startsWith(PUBLISH_ROUTING_KEY_PREFIX)) {
            String recipient = receivedRoutingKey.substring(PUBLISH_ROUTING_KEY_PREFIX.length());
            logger.info("PUBLISH: ---[{}]--->{}. Body: {}", routingKeys, recipient, body);
        } else if(receivedRoutingKey.startsWith(DELIVER_ROUTING_KEY_PREFIX)) {
            String recipient = receivedRoutingKey.substring(DELIVER_ROUTING_KEY_PREFIX.length());
            logger.info("DELIVER: {} ---[{}]--->{}. Body: {}", exchangeName, routingKeys, recipient, body);
        } else {
            logger.info("{} ---[{}]--->{}. Body: {}", exchangeName, routingKeys, receivedRoutingKey, body);
        }
    }

    private Binding bindTraceExchange(Queue loggerQueue) {
        Binding binding = new Binding(loggerQueue.getName(), Binding.DestinationType.QUEUE, "amq.rabbitmq.trace", "#", Collections.emptyMap());
        logger.info("Binding {} exchange to {}", "amq.rabbitmq.trace", binding);
        return binding;
    }

}
