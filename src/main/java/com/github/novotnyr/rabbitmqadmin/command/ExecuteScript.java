package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
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
            Yaml yaml = new Yaml();
            Iterable<Object> documents = (Iterable<Object>) yaml.loadAll(new FileReader(this.scriptFile));
            Iterator<Object> iterator = documents.iterator();
            if (!iterator.hasNext()) {
                stdErr.println("No configuration section in script");
                return;
            }
            Map<String, Object> configuration = (Map<String, Object>) iterator.next();
            RabbitConfiguration rabbitConfiguration = parseConfiguration(configuration);

            if (!iterator.hasNext()) {
                stdErr.println("No script section in script");
                return;
            }

            while (iterator.hasNext()) {
                Map<String, Object> scriptDocument = (Map<String, Object>) iterator.next();
                if (scriptDocument.containsKey("publish")) {
                    handlePublishToExchange(rabbitConfiguration, scriptDocument);
                } else {
                    stdErr.println("Unsupported command type in " + this.scriptFile);
                }
            }

        } catch (FileNotFoundException e) {
            stdErr.println("Cannot find script " + this.scriptFile);
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
        if (script.containsKey("reply-to")) {
            command.setReplyTo((String) script.get("reply-to"));
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

    public void setStdErr(StdErr stdErr) {
        this.stdErr = stdErr;
    }
}
