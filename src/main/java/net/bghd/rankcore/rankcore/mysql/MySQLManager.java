package net.bghd.rankcore.rankcore.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.bghd.rankcore.rankcore.Manager;
import net.bghd.rankcore.rankcore.RankCore;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLManager extends Manager {

    private HikariDataSource hikariDataSource;
    private RankCore plugin = RankCore.getInstance();

    public MySQLManager(RankCore plugin) {
        super(plugin);
        connect();
        createTable();
    }

    public Exception connect() {
        try {
            //Connect method
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/youtubetest");
            hikariConfig.setUsername("test");
            hikariConfig.setPassword("test");
            hikariDataSource = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            hikariDataSource = null;
            return e;
        }
        return null;
    }

    public void createTable() {
        createTable("player", "uuid VARCHAR(36) NOT NULL PRIMARY KEY, name VARCHAR(16), rank VARCHAR(20)");
    }

    public void shutdown() {
        close();
    }

    public boolean isInitiated() {
        return hikariDataSource != null;
    }

    public void close() {
        this.hikariDataSource.close();
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    /**
     * Create a new table in the database.
     *
     * @param name The name of the table.
     * @param info The table info between the round VALUES() brackets.
     */
    public void createTable(String name, String info) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + "(" + info + ");")) {
                statement.execute();
            } catch (SQLException exception) {
                Bukkit.getConsoleSender().sendMessage("An error occurred while creating database table " + name + ".");
                exception.printStackTrace();
            }
        }).start();
    }

    /**
     * Execute an update to the database.
     *
     * @param query  The statement to the database.
     * @param values The values to be inserted into the statement.
     */
    public void execute(String query, Object... values) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i + 1), values[i]);
                }
                statement.execute();
            } catch (SQLException exception) {
                Bukkit.getConsoleSender().sendMessage("An error occurred while executing an update on the database.");
                Bukkit.getConsoleSender().sendMessage("MySQL#execute : " + query);
                exception.printStackTrace();
            }
        }).start();
    }

    /**
     * Execute a query to the database.
     *
     * @param query    The statement to the database.
     * @param callback The data callback (Async).
     * @param values   The values to be inserted into the statement.
     */
    public void select(String query, SelectCall callback, Object... values) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i + 1), values[i]);
                }
                callback.call(statement.executeQuery());
            } catch (SQLException exception) {
                Bukkit.getConsoleSender().sendMessage("An error occurred while executing a query on the database.");
                Bukkit.getConsoleSender().sendMessage("MySQL#select : " + query);
                exception.printStackTrace();
            }
        }).start();
    }


}
