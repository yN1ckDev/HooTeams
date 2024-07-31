package it.n1ckcode.hooteams.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.n1ckcode.hooteams.HooTeams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.logging.Logger;

public class DatabaseManager {

    private static HikariDataSource dataSource;

    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + HooTeams.getFileManager().getConfig().getString("database.host") + ":" + HooTeams.getFileManager().getConfig().getString("database.port") + "/" + HooTeams.getFileManager().getConfig().getString("database.name"));
        config.setUsername(HooTeams.getFileManager().getConfig().getString("database.username"));
        config.setPassword(HooTeams.getFileManager().getConfig().getString("database.password"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        dataSource = new HikariDataSource(config);

        Bukkit.getLogger().info("[Connection] Connection to database established.");

        createTables();
    }

    private void createTables() {
        String createClansTable = "CREATE TABLE IF NOT EXISTS clans (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "creator VARCHAR(36) NOT NULL, " +
                "name VARCHAR(50) NOT NULL, " +
                "moderators TEXT, " +
                "members TEXT, " +
                "UNIQUE KEY (creator)" +
                ");";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createClansTable);
            Bukkit.getLogger().info("[Database] Table created successfully.");;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createClan(String creator, String name) {
        String query = "INSERT INTO clans (creator, name) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, creator);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void renameClan(String oldName, String newName) {
        String query = "UPDATE clans SET name = ? WHERE name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newName);
            statement.setString(2, oldName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean canCreateClan(Player player) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT COUNT(*) AS count FROM clans WHERE creator = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("count") == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean disbandClan(String creatorUUID) {
        String sql = "DELETE FROM clans WHERE creator = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, creatorUUID);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasClan(String playerUUID) {
        String sql = "SELECT 1 FROM clans WHERE creator = ? OR FIND_IN_SET(?, members) > 0";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUUID);
            statement.setString(2, playerUUID);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            Bukkit.getLogger().info("[Connection] Connection to database closed.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
