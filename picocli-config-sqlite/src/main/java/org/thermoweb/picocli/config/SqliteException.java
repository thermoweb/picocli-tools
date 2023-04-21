package org.thermoweb.picocli.config;

public class SqliteException extends RuntimeException {
    public SqliteException(String message, Exception e) {
        super(message, e);
    }
}
