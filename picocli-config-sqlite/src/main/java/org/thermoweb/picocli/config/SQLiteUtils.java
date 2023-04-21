package org.thermoweb.picocli.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class SQLiteUtils {

    private SQLiteUtils() {

    }

    public static void createNewDatabase(String filename) {
        Path dbLocation = Paths.get(filename).getParent();
        createDatabaseDirectory(dbLocation);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + filename)) {
            if (conn != null) {
                log.atInfo().log("connected to {}", filename);
            }
        } catch (SQLException e) {
            log.atError().log("Error : {}", e.getMessage());
            throw new SqliteException(e.getMessage(), e);
        }
    }

    private static void createDatabaseDirectory(Path dbLocation) {
        try {
            Files.createDirectories(dbLocation);
        } catch (IOException e) {
            String errorMessage = "Error Creating db directory : " + dbLocation;
            log.atError().log(errorMessage);
            throw new SqliteException(errorMessage, e);
        }
    }

    public static Connection getConnection(String filename) throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + filename);
    }
}
