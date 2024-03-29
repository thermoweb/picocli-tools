package org.thermoweb.picocli.config;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class Config {

    private static ConfigHolder configHolder = new NoOpConfigHolder();

    private Config() {
    }

    public static void setConfigHolder(ConfigHolder configHolder) {
        Config.configHolder = configHolder;
    }

    public static void setProperty(String name, String value) {
        setProperty(name, value, false);
    }

    public static void setProperty(String name, String value, boolean isSecret) {
        configHolder.setProperty(new SimpleProperty(name, value, isSecret));
    }

    public static void setProperty(Property property, String value) {
        configHolder.setProperty(new SimpleProperty(property.id(), value, property.isSecret()));
    }

    public static void setProperty(Property property) {
        configHolder.setProperty(property);
    }

    public static Property getProperty(Property property) {
        return configHolder.getProperty(property).orElseThrow();
    }

    public static Optional<Property> get(Property property) {
        return configHolder.getProperty(property);
    }

    public static String getPropertyAsString(Property property) {
        return configHolder.getPropertyAsString(property).orElseThrow();
    }

    public static List<Property> list() {
        return configHolder.list();
    }

    private static class NoOpConfigHolder implements ConfigHolder {
        private static final String ERROR_MESSAGE = "no configuration holder set.";

        @Override
        public void setProperty(Property property) {
            log.atError().log(ERROR_MESSAGE);
            throw new ConfigException(ERROR_MESSAGE);
        }

        @Override
        public Optional<Property> getProperty(Property property) {
            log.atWarn().log(ERROR_MESSAGE);
            return Optional.empty();
        }

        @Override
        public Optional<Property> getProperty(String propertyName) {
            log.atWarn().log(ERROR_MESSAGE);
            return Optional.empty();
        }

        @Override
        public List<Property> list() {
            log.atWarn().log(ERROR_MESSAGE);
            return Collections.emptyList();
        }
    }
}
