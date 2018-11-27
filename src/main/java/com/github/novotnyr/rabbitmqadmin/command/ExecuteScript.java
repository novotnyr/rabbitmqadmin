package com.github.novotnyr.rabbitmqadmin.command;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.github.novotnyr.rabbitmqadmin.command.script.RabbitConfigurationParser;
import com.github.novotnyr.rabbitmqadmin.command.script.Script;
import com.github.novotnyr.rabbitmqadmin.log.StdErr;
import com.github.novotnyr.rabbitmqadmin.log.SystemErr;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExecuteScript {
    private StdErr stdErr = new SystemErr();

    private RabbitConfigurationParser rabbitConfigurationParser = new RabbitConfigurationParser();

    private final RabbitConfiguration rabbitConfiguration;

    private String scriptFile;

    private List<Integer> excludedCommandIndices = new ArrayList<>();

    private List<Integer> includedCommandIndices = new ArrayList<>();

    public ExecuteScript(RabbitConfiguration rabbitConfiguration) {
        this.rabbitConfiguration = rabbitConfiguration;
    }

    public void run() {
        try {
            Script script = new Script();

            Yaml yaml = new Yaml();
            Iterable<Object> documents = (Iterable<Object>) yaml.loadAll(new FileReader(this.scriptFile));
            Iterator<Object> iterator = documents.iterator();
            RabbitConfiguration rabbitConfiguration;
            Map<String, Object> configuration = (Map<String, Object>) iterator.next();
            if (this.rabbitConfiguration != null) {
                rabbitConfiguration = this.rabbitConfiguration;
            } else {
                rabbitConfiguration = this.rabbitConfigurationParser.parseConfiguration(configuration);
            }
            script.setConfiguration(rabbitConfiguration);

            while (iterator.hasNext()) {
                Map<String, Object> scriptDocument = (Map<String, Object>) iterator.next();
                if (scriptDocument.containsKey("publish")) {
                    PublishToExchange publishToExchange = parsePublishToExchange(rabbitConfiguration, scriptDocument);
                    script.append(publishToExchange);
                } else if (scriptDocument.containsKey("get")) {
                    GetMessage getMessage = parseGetMessage(rabbitConfiguration, scriptDocument);
                    script.append(getMessage);
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
        int index = 0;
        for (Command<?> command : script.getCommands()) {
            if (excludedCommandIndices.contains(index)) {
                continue;
            }
            if (includedCommandIndices.isEmpty() || includedCommandIndices.contains(index)) {
                Object result = command.run();
                stdErr.println(result.toString());
            }
            index++;
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

    private GetMessage parseGetMessage(RabbitConfiguration rabbitConfiguration, Map<String, Object> script) {
        GetMessage command = new GetMessage(rabbitConfiguration);
        command.setQueue((String) script.get("get"));
        return command;
    }


    public void setScriptFile(String scriptFile) {
        this.scriptFile = scriptFile;
    }

    public void setStdErr(StdErr stdErr) {
        this.stdErr = stdErr;
    }

    public void setExcludedCommandIndices(List<Integer> excludedCommandIndices) {
        this.excludedCommandIndices = excludedCommandIndices;
    }

    public void setIncludedCommandIndices(List<Integer> includedCommandIndices) {
        this.includedCommandIndices = includedCommandIndices;
    }

    public void setRabbitConfigurationParser(RabbitConfigurationParser rabbitConfigurationParser) {
        this.rabbitConfigurationParser = rabbitConfigurationParser;
    }
}
