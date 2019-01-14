package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.command.script.ScriptOutputSerializer;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

public class ExecuteScriptTest {
    @Test
    public void test() {
        ExecuteScript executeScript = new ExecuteScript(null);
        executeScript.setScriptFile("src/test/resources/example.get.rabbitmq");
        TypeToken TYPE_TOKEN = new TypeToken<Collection<RetrievedMessage>>(){};
        executeScript.setOutputSerializer(GetMessage.class, new ScriptOutputSerializer<List<RetrievedMessage>>() {
            @Override
            public void serialize(Class<?> type, List<RetrievedMessage> retrievedMessages) {
                for (RetrievedMessage message : retrievedMessages) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Messages left in Queue: ").append(message.getMessagesLeftInQueue()).append("\n");
                    sb.append("Payload Size: ").append(message.getPayloadSize()).append("\n");
                    sb.append("Redelivered: ").append(message.isRedelivered() ? "yes" : "no").append("\n");
                    sb.append("Exchange: ").append(message.getExchange()).append("\n");
                    sb.append("Payload: ").append(message.getPayload());

                    System.out.println(sb);
                }
            }
        });
        executeScript.run();
    }

    @Test
    public void testPublish() {
        ExecuteScript executeScript = new ExecuteScript(null);
        executeScript.setScriptFile("src/test/resources/example.publish.rabbitmq");
        executeScript.run();
    }
}