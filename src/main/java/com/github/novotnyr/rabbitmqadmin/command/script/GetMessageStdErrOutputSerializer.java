package com.github.novotnyr.rabbitmqadmin.command.script;

import com.github.novotnyr.rabbitmqadmin.command.Command;
import com.github.novotnyr.rabbitmqadmin.command.RetrievedMessage;
import com.github.novotnyr.rabbitmqadmin.log.StdErr;

import java.util.List;
import java.util.Map;

public class GetMessageStdErrOutputSerializer implements ScriptOutputSerializer<List<RetrievedMessage>> {
    private final StdErr stdErr;

    public GetMessageStdErrOutputSerializer(StdErr stdErr) {
        this.stdErr = stdErr;
    }

    @Override
    public void serialize(Command<?> command, List<RetrievedMessage> retrievedMessages) {
        for (RetrievedMessage message : retrievedMessages) {
            StringBuilder sb = new StringBuilder();
            sb.append("Messages left in Queue: ").append(message.getMessagesLeftInQueue()).append("\n");
            sb.append("Payload Size: ").append(message.getPayloadSize()).append("\n");
            sb.append("Redelivered: ").append(message.isRedelivered() ? "yes" : "no").append("\n");
            sb.append("Exchange: ").append(message.getExchange()).append("\n");
            Map<String, String> headers = message.getProperties().getHeaders();
            if (!headers.isEmpty()) {
                sb.append("Headers:\n");
                headers.forEach((name, value) -> {
                    sb.append("  ").append(name).append(": ").append(value).append("\n");
                });
            }
            sb.append("Payload: ").append(message.getPayload());

            stdErr.println(sb.toString());
        }
    }
}
