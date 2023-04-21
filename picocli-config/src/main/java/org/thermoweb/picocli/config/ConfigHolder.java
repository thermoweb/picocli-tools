package org.thermoweb.picocli.config;

import java.util.List;
import java.util.Optional;

public interface ConfigHolder {

    void setProperty(Property property, String value);
    Optional<String> getProperty(Property property);
    List<Property> list();
}
