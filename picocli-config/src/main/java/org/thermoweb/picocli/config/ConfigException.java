package org.thermoweb.picocli.config;

public class ConfigException extends RuntimeException {

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(Exception e) {
        super(e);
    }
}
