package org.thermoweb.picocli.config;

public interface Property {
    String id();
    String value();
    default boolean isSecret() {
        return false;
    }
}
