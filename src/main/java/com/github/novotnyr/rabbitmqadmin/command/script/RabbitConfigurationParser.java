package com.github.novotnyr.rabbitmqadmin.command.script;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.github.novotnyr.rabbitmqadmin.command.UnknownProtocolException;

import java.util.Map;
import java.util.Optional;

public class RabbitConfigurationParser {
    public boolean isConfiguration(Map<String, Object> configurationMap) {
        return configurationMap.containsKey("host");
    }

    public RabbitConfiguration parseConfiguration(Map<String, Object> configurationMap) {
        RabbitConfiguration configuration = new RabbitConfiguration();
        configuration.setProtocol(parseProtocol(configurationMap));
        deducePortFromProtocol(configuration, configurationMap);
        parseValue(configurationMap, "host").ifPresent(configuration::setHost);
        parseValue(configurationMap, "user").ifPresent(configuration::setUser);
        parseValue(configurationMap, "password").ifPresent(configuration::setPassword);
        parseValue(configurationMap, "vhost").ifPresent(configuration::setVirtualHost);

        return configuration;
    }

    private Optional<String> parseValue(Map<String, Object> configurationMap, String key) {
        Object valueObject = configurationMap.get(key);
        if (!(valueObject instanceof String)) {
            return Optional.empty();
        }
        return Optional.of((String) valueObject);
    }

    private void deducePortFromProtocol(RabbitConfiguration configuration, Map<String, Object> configurationMap) {
        Object portObject = configurationMap.get("port");
        if (portObject instanceof Integer) {
            int port = (Integer) portObject;
            configuration.setPort(port);
            return;
        } else {
            switch (configuration.getProtocol()) {
                case HTTP:
                    configuration.setPort(RabbitConfiguration.DEFAULT_HTTP_PORT);
                    break;
                case HTTPS:
                    configuration.setPort(RabbitConfiguration.DEFAULT_HTTPS_PORT);
                    break;
            }
        }
    }

    private RabbitConfiguration.Protocol parseProtocol(Map<String, Object> configurationMap) throws UnknownProtocolException {
        String protocolString = parseValue(configurationMap, "protocol")
                .orElse(RabbitConfiguration.Protocol.HTTP.name());
        try {
            RabbitConfiguration.Protocol protocol = RabbitConfiguration.Protocol.valueOf(protocolString.toUpperCase());
            return protocol;
        } catch (IllegalArgumentException e) {
            throw new UnknownProtocolException("Unsupported protocol " + protocolString);
        }
    }
}
