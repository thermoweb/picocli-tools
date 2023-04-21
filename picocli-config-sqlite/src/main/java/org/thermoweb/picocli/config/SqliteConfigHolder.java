package org.thermoweb.picocli.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SqliteConfigHolder implements ConfigHolder {

    private final String databaseName;

    public SqliteConfigHolder(String databaseName) {
        this.databaseName = databaseName;
        init(Collections.emptyList());
    }

    public SqliteConfigHolder(String databaseName, List<Property> properties) {
        this.databaseName = databaseName;
        init(properties);
    }

    @Override
    public void setProperty(Property property, String value) {
        String sql = "INSERT OR REPLACE INTO properties(id, value, secret) VALUES (?,?,?);";

        try (Connection connection = SQLiteUtils.getConnection(databaseName);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, property.id().toUpperCase());
            preparedStatement.setString(2, value);
            preparedStatement.setBoolean(3, property.isSecret());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ConfigException(e);
        }
    }

    @Override
    public Optional<String> getProperty(Property property) {
        String sql = "SELECT value FROM properties WHERE id = ?";
        try (Connection connection = SQLiteUtils.getConnection(databaseName);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, property.id());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSet.getString("value"));
            }

        } catch (SQLException e) {
            throw new ConfigException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Property> list() {
        String sql = "SELECT id, value, secret FROM properties;";
        List<Property> properties = new ArrayList<>();
        try (Connection connection = SQLiteUtils.getConnection(databaseName);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                properties.add(new SqliteProperty(resultSet.getString("id"), resultSet.getString("value"), resultSet.getBoolean("secret")));
            }
            return properties;
        } catch (SQLException e) {
            throw new ConfigException(e);
        }
    }

    private void init(List<Property> properties) {
        SQLiteUtils.createNewDatabase(databaseName);
        try (Connection connection = SQLiteUtils.getConnection(databaseName)) {
            createPropertiesTable(connection);
            insertPropertiesData(connection, properties);
        } catch (SQLException e) {
            throw new ConfigException(e);
        }
    }

    private static void insertPropertiesData(Connection connection, List<Property> properties) {
        if (properties.isEmpty()) {
            return;
        }

        String sql = "INSERT OR IGNORE INTO properties (id, secret) VALUES (?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Property property : properties) {
                preparedStatement.setString(1, property.id());
                preparedStatement.setBoolean(2, property.isSecret());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new ConfigException(e);
        }
    }

    private static void createPropertiesTable(Connection connection) {
        String sql = "CREATE TABLE IF NOT EXISTS properties (id text PRIMARY KEY, value text, secret integer not null default 0);";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new ConfigException(e);
        }
    }

    private record SqliteProperty(String id, String value, boolean isSecret) implements Property {
    }
}
