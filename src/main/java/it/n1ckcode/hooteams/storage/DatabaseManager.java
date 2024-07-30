package it.n1ckcode.hooteams.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.n1ckcode.hooteams.HooTeams;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseManager {

    private HikariDataSource dataSource;

    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + HooTeams.getFileManager().getConfig().getString("database.host" + ":" + HooTeams.getFileManager().getConfig().getString("database.port")) + "/" + HooTeams.getFileManager().getConfig().getString("database.name"));
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
        String createClansTable = "CREATE TABLE IF NOT EXISTS teams ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "creator VARCHAR(255) NOT NULL, "
                + "name VARCHAR(255) NOT NULL, "
                + "moderators TEXT NOT NULL"
                + ");";

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

    public void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            Bukkit.getLogger().info("[Connection] Connection to database closed.");
        }
    }
}
