package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.github.novotnyr.rabbitmqadmin.command.script.Script;
import com.github.novotnyr.rabbitmqadmin.log.StdErr;
import com.github.novotnyr.rabbitmqadmin.log.SystemErr;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

public class ExecuteScript {
    private StdErr stdErr = new SystemErr();

    private final RabbitConfiguration rabbitConfiguration;

    private String scriptFile;

    public ExecuteScript(RabbitConfiguration rabbitConfiguration) {
        this.rabbitConfiguration = rabbitConfiguration;
    }

    public void run() {
        try {
            Script script = new Script();

            Yaml yaml = new Yaml();
            Iterable<Object> documents = (Iterable<Object>) yaml.loadAll(new FileReader(this.scriptFile));
            Iterator<Object> iterator = documents.iterator();
            Map<String, Object> configuration = (Map<String, Object>) iterator.next();
            script.setConfiguration(parseConfiguration(configuration));

            while (iterator.hasNext()) {
                Map<String, Object> scriptDocument = (Map<String, Object>) iterator.next();
                if (scriptDocument.containsKey("publish")) {
                    PublishToExchange publishToExchange = parsePublishToExchange(rabbitConfiguration, scriptDocument);
                    script.append(publishToExchange);
                } else {
                    stdErr.println("Unsupported command type in " + this.scriptFile);
                }
            }
            validate(script);
            doRun(script);
        } catch (FileNotFoundException e) {
            stdErr.println("Cannot find script " + this.scriptFile);
        }
    }

    public boolean validate(Script script) {
        if (script.getConfiguration() == null) {
            stdErr.println("No RabbitMQ connection configuration in script");
            return false;
        }
        if (script.getCommands().isEmpty()) {
            stdErr.println("No script section in script");
            return false;
        }

        return true;
    }

    public void doRun(Script script) {
        for (Command<?> command : script.getCommands()) {
            command.run();
        }
    }

    private PublishToExchange parsePublishToExchange(RabbitConfiguration rabbitConfiguration, Map<String, Object> script) {
        PublishToExchange command = new PublishToExchange(rabbitConfiguration);
        command.setRoutingKey((String) script.get("routing-key"));
        command.setExchange((String) script.get("publish"));
        if (script.containsKey("json")) {
            command.encodeAndSetUtf8Contents((String) script.get("json"));
            command.setContentType("application/json");
        } else {
            command.encodeAndSetUtf8Contents((String) script.get("payload"));
        }
        if (script.containsKey("reply-to")) {
            command.setReplyTo((String) script.get("reply-to"));
        }
        return command;
    }

    private RabbitConfiguration parseConfiguration(Map<String, Object> configurationMap) {
        RabbitConfiguration configuration = new RabbitConfiguration();
        configuration.setHost((String) configurationMap.get("host"));
        configuration.setUser((String) configurationMap.get("user"));
        configuration.setPassword((String) configurationMap.get("password"));
        configuration.setVirtualHost((String) configurationMap.get("vhost"));

        return configuration;
    }

    public void setScriptFile(String scriptFile) {
        this.scriptFile = scriptFile;
    }

    public void setStdErr(StdErr stdErr) {
        this.stdErr = stdErr;
    }
}
