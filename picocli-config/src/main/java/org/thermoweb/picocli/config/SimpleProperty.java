package org.thermoweb.picocli.config;

public record SimpleProperty(String id, String value, boolean isSecret) implements Property {
    public SimpleProperty(String property) {
        this(property, null, false);
    }
}
