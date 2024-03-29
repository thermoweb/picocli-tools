package org.thermoweb.picocli.command;

import lombok.extern.slf4j.Slf4j;
import org.thermoweb.picocli.config.Config;
import org.thermoweb.picocli.config.Property;
import org.thermoweb.picocli.config.SimpleProperty;
import picocli.CommandLine;

import java.util.List;

/**
 * Command class to add to enable the config command into your tool. Simply add it into your subcommands.
 */
@Slf4j
@CommandLine.Command(name = "config")
public class ConfigCommand implements Runnable {

    /** This is the string value that secret properties will be replaced with. */
    private static final String SECRET_PLACEHOLDER = "<secret>";

    @CommandLine.Parameters(index = "0", arity = "0..1")
    private String propertyName;

    @CommandLine.Parameters(index = "1", arity = "0..1")
    private String propertyValue;

    @CommandLine.Option(names = "--secret")
    private boolean isSecret;

    public ConfigCommand() {
        log.atDebug().log();
    }

    @Override
    public void run() {
        Config.setProperty(new SimpleProperty(propertyName, propertyValue, isSecret));
        log.atInfo().log("property '{}' set to '{}'", propertyName, isSecret ? SECRET_PLACEHOLDER : propertyValue);
    }

    @CommandLine.Command(name = "list", description = "list all properties")
    public void list() {
        List<Property> properties = Config.list();
        properties.forEach(p -> log.atInfo().log(String.format("%s : %s", p.id(), p.isSecret() ? SECRET_PLACEHOLDER : p.value())));
    }

}
