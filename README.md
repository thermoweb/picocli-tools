# Picocli-tools


## Description

Some tools to help [picocli](https://picocli.info/) integration.
- picocli-config : add the config command to your tool to store and use configuration.
- picocli-config-sqlite : implementation of picocli-config that uses sqlite to store the configuration.

## Getting Started

Add the following into your pom.xml : 
```xml
<dependency>
    <groupId>org.thermoweb.picocli</groupId>
    <artifactId>picocli-config-sqlite</artifactId>
    <version>0.1</version>
</dependency>
```

Add the `ConfigCommand` in the subcommands of your tool.
Set the `ConfigHolder` that will stores the configuration.

*here an exemple with Sqlite configuration holder :*
```java
Config.setConfigHolder(new SqliteConfigHolder(".db/mytool.db", properties));
```

Now you can use it in your tool like this : 
```shell
mytool config list
mytool config myProperty myValue
mytool config mySecretProperty mySecretValue --secret
```

To use get configuration property in your code, just use `Config.getProperty("myProperty")`.

## Version History

## License

This project is licensed under the MIT License - see the LICENSE file for details