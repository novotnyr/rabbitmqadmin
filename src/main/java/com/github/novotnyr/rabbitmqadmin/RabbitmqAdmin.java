package com.github.novotnyr.rabbitmqadmin;

import com.github.novotnyr.rabbitmqadmin.command.DeclareBinding;
import com.github.novotnyr.rabbitmqadmin.command.DeclareExchange;
import com.github.novotnyr.rabbitmqadmin.command.ExecuteScript;
import com.github.novotnyr.rabbitmqadmin.command.ListExchanges;
import com.github.novotnyr.rabbitmqadmin.command.ListQueues;
import com.github.novotnyr.rabbitmqadmin.command.PublishToExchange;
import com.github.novotnyr.rabbitmqadmin.command.PublishToExchangeResponse;
import com.github.novotnyr.rabbitmqadmin.command.Trace;
import com.github.novotnyr.rabbitmqadmin.command.WhoAmI;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.rabbitmq.client.ConnectionFactory.DEFAULT_PASS;
import static com.rabbitmq.client.ConnectionFactory.DEFAULT_USER;

public class RabbitmqAdmin {
    @Option(name = "-H")
    private String host = com.rabbitmq.client.ConnectionFactory.DEFAULT_HOST;

    @Option(name = "-u", aliases = "--user")
    private String user = DEFAULT_USER;

    @Option(name = "-p", aliases = "--password")
    private String password = DEFAULT_PASS;

    @Option(name = "-type")
    private String exchangeType;

    @Option(name = "-name")
    private String name;

    @Option(name = "-durable")
    private boolean durable = true;

    @Option(name = "-autodelete")
    private boolean autoDelete = false;

    @Option(name = "-V", aliases = "--virtual-host")
    private String virtualHost = "/";

    @Option(name = "-source")
    private String source;

    @Option(name = "-destination")
    private String destination;

    @Option(name = "-routing_key")
    private String routingKey;

    @Option(name = "-data_binary", aliases = "--data-binary")
    private String binaryData;

    @Option(name = "-content_type", aliases = "--content-type")
    private String contentType;

    @Option(name = "-reply_to", aliases = "--reply-to")
    private String replyTo;

    @Option(name = "-f", aliases = "--script")
    private String scriptFile;

    @Argument
    private List<String> arguments = new ArrayList<String>();

    private RabbitConfiguration rabbitConfiguration;

    public void doMain(String[] args) {
        CmdLineParser parser = null;
        try {
            parser = new CmdLineParser(this);
            parser.parseArgument(args);
            if(arguments.isEmpty()) {
                throw new CmdLineException(parser, "Missing admin command");
            }
            this.rabbitConfiguration = parseConfiguration(parser);
            handle(parser);

        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java RabbitmqAdmin [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println();

            System.err.println("  Example: java RabbitmqAdmin" + parser.printExample(OptionHandlerFilter.ALL));
            return;
        }
    }

    private RabbitConfiguration parseConfiguration(CmdLineParser parser) {
        RabbitConfiguration rabbitConfiguration = new RabbitConfiguration();
        rabbitConfiguration.setHost(this.host);
        rabbitConfiguration.setVirtualHost(this.virtualHost);
        rabbitConfiguration.setUser(this.user);
        rabbitConfiguration.setPassword(this.password);

        return rabbitConfiguration;
    }

    private void handle(CmdLineParser parser) throws CmdLineException {
        String command = arguments.get(0);
        switch(command) {
            case "list" :
                handleList(parser);
                break;
            case "declare" :
                handleDeclare(parser, arguments);
                break;
            case "trace" :
                handleTrace(parser, arguments);
                break;
            case "publish" :
                handlePublish(parser, arguments);
                break;
            case "whoami" :
                handleWhoAmI(parser, arguments);
                break;
            case "exec" :
                handleExecuteScript(parser, arguments);
                break;
            default:
                throw new CmdLineException(parser, "Unknown command " + command);
        }
    }

    private void handleExecuteScript(CmdLineParser parser, List<String> arguments) {
        ExecuteScript executeScript = new ExecuteScript(this.rabbitConfiguration);
        executeScript.setScriptFile(this.scriptFile);
        executeScript.run();
    }

    private void handleWhoAmI(CmdLineParser parser, List<String> arguments) {
        WhoAmI whoAmI = new WhoAmI(this.rabbitConfiguration);
        String result = whoAmI.run();
        System.out.println(result);
    }

    private void handleTrace(CmdLineParser parser, List<String> arguments) {
        Trace trace = new Trace();
        trace.setConfiguration(this.rabbitConfiguration);
        trace.run();
    }

    private void handleList(CmdLineParser parser) throws CmdLineException {
        String entityType = arguments.get(1);
        switch(entityType) {
            case "exchanges":
                ListExchanges listExchanges = new ListExchanges(this.rabbitConfiguration);
                List<Exchange> exchanges = listExchanges.run();
                System.out.println(exchanges);
                break;
            case "queues":
                ListQueues listQueues = new ListQueues(this.rabbitConfiguration);
                List<Queue> queues = listQueues.run();
                System.out.println(queues);
                break;
            default:
                throw new CmdLineException(parser, "Unknown entity type for listing " + entityType);
        }
    }

    private void handleDeclare(CmdLineParser parser, List<String> arguments) throws CmdLineException {
        String entityType = arguments.get(1);
        switch (entityType) {
            case "exchange":
                DeclareExchange declareExchange = new DeclareExchange();
                declareExchange.setConfiguration(rabbitConfiguration);
                declareExchange.setName(this.name);
                declareExchange.setType(this.exchangeType);
                declareExchange.setAutoDelete(this.autoDelete);
                declareExchange.setDurable(this.durable);

                declareExchange.run();
                break;
            case "binding":
                DeclareBinding declareBinding = new DeclareBinding(this.rabbitConfiguration);
                declareBinding.setExchange(this.source);
                declareBinding.setQueue(this.destination);
                declareBinding.setRoutingKey(this.routingKey);

                declareBinding.run();
                break;
            default:
                throw new CmdLineException(parser, "Unknown entity type " + entityType);
        }
    }

    private void handlePublish(CmdLineParser parser, List<String> arguments) {
        PublishToExchange command = new PublishToExchange(this.rabbitConfiguration);
        if (this.destination == null) {
            System.err.println("Destination exchange (-destination) must be set");
            return;
        }
        command.setExchange(this.destination);

        if (this.routingKey == null) {
            System.err.println("Routing key (-routing_key) must be set");
            return;
        }
        command.setRoutingKey(this.routingKey);
        if(this.binaryData != null) {
            if(this.binaryData.startsWith("@")) {
                try {
                    Path path = Paths.get(this.binaryData.substring(1));
                    command.encodeAndSetUtf8Contents(Files.readAllBytes(path));
                } catch (IOException e) {
                    System.err.println("Cannot load binary data: " + this.binaryData);
                    return;
                }
            } else {
                command.encodeAndSetUtf8Contents(this.binaryData);
            }
        } else {
            System.err.println("Binary data (-data-binary) must be set: ");
            return;
        }
        if (this.contentType != null) {
            command.setContentType(this.contentType);
        }
        if (this.replyTo != null) {
            command.setReplyTo(this.replyTo);
        }
        PublishToExchangeResponse reply = command.run();
        if (reply.isRouted()) {
            System.out.println("Message has been routed");
        } else {
            System.out.println("Message has *not* been routed");
        }
    }


    public static void main(String[] args) {
        new RabbitmqAdmin().doMain(args);
    }
}
