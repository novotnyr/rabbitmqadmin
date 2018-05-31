package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

public class ExecuteScript {
    private final RabbitConfiguration rabbitConfiguration;

    private String scriptFile;

    public ExecuteScript(RabbitConfiguration rabbitConfiguration) {
        this.rabbitConfiguration = rabbitConfiguration;
    }

    public void run() {
        try {
            Yaml yaml = new Yaml();
            Iterable<Object> documents = (Iterable<Object>) yaml.loadAll(new FileReader(this.scriptFile));
            Iterator<Object> iterator = documents.iterator();
            if (!iterator.hasNext()) {
                System.err.println("No configuration section in script");
                return;
            }
            Map<String, Object> configuration = (Map<String, Object>) iterator.next();
            RabbitConfiguration rabbitConfiguration = parseConfiguration(configuration);

            if (!iterator.hasNext()) {
                System.err.println("No script section in script");
                return;
            }

            Map<String, Object> firstScript = (Map<String, Object>) iterator.next();
            if (firstScript.containsKey("publish")) {
                handlePublishToExchange(rabbitConfiguration, firstScript);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Cannot find script " + this.scriptFile);
        }
    }

    private void handlePublishToExchange(RabbitConfiguration rabbitConfiguration, Map<String, Object> script) {
        PublishToExchange command = new PublishToExchange(rabbitConfiguration);
        command.setRoutingKey((String) script.get("routing-key"));
        command.setExchange((String) script.get("publish"));
        if (script.containsKey("json")) {
            command.encodeAndSetUtf8Contents((String) script.get("json"));
            command.setContentType("application/json");
        } else {
            command.encodeAndSetUtf8Contents((String) script.get("payload"));
        }

        command.run();
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
}
