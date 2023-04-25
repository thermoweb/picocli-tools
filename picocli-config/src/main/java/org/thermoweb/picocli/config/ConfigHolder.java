package org.thermoweb.picocli.config;

import java.util.List;
import java.util.Optional;

public interface ConfigHolder {

    void setProperty(Property property);

    Optional<Property> getProperty(Property property);

    default Optional<String> getPropertyAsString(Property property) {
        return getProperty(property).map(Property::value);
    }

    Optional<Property> getProperty(String propertyName);

    default Optional<String> getPropertyAsString(String propertyName) {
        return getProperty(propertyName).map(Property::value);
    }

    List<Property> list();
}
