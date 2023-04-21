package org.thermoweb.picocli.command;

import lombok.extern.slf4j.Slf4j;
import org.thermoweb.picocli.config.Config;
import org.thermoweb.picocli.config.Property;
import picocli.CommandLine;

import java.util.List;

@Slf4j
@CommandLine.Command(name = "config")
public class ConfigCommand implements Runnable {

    private static final String SECRET_PLACEHOLDER = "<secret>";
    @CommandLine.Parameters(index = "0", arity = "0..1")
    private String propertyName;

    @CommandLine.Parameters(index = "1", arity = "0..1")
    private String propertyValue;

    @CommandLine.Option(names = "--secret")
    private boolean isSecret;

    @Override
    public void run() {
        Config.setProperty(new SimpleProperty(propertyName, isSecret), propertyValue);
        log.atInfo().log("property '{}' set to '{}'", propertyName, isSecret ? SECRET_PLACEHOLDER :propertyValue);
    }

    @CommandLine.Command(name = "list", description = "list all properties")
    public void list() {
        List<Property> properties = Config.list();
        properties.forEach(p -> log.atInfo().log(String.format("%s : %s", p.id(), p.isSecret() ? SECRET_PLACEHOLDER :p.value())));
    }

    record SimpleProperty(String id, boolean isSecret) implements Property {
        @Override
        public String value() {
            return null;
        }
    }
}
