package br.neobydeveloper.com.redeskycore.Mysql;

import br.neobydeveloper.com.redeskycore.Manager;
import br.neobydeveloper.com.redeskycore.RedeSkyCore;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class MySQLManager extends Manager {
    private HikariDataSource hikariDataSource;
    private final RedeSkyCore plugin = RedeSkyCore.getInstance();

    public MySQLManager(RedeSkyCore plugin) {
        super(plugin);
        if (this.connect() == null) {
            this.createTable();
        }

    }

    public Exception connect() {
        try {
            FileConfiguration config = this.plugin.getConfig();
            String host = config.getString("mysql.host");
            int port = config.getInt("mysql.port");
            String database = config.getString("mysql.database");
            String username = config.getString("mysql.username");
            String password = config.getString("mysql.password");
            boolean useSSL = config.getBoolean("mysql.useSSL", false);
            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + useSSL + "&autoReconnect=true";
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            this.hikariDataSource = new HikariDataSource(hikariConfig);
            return null;
        } catch (Exception e) {
            this.hikariDataSource = null;
            e.printStackTrace();
            return e;
        }
    }

    public void createTable() {
        this.createTable("player", "uuid VARCHAR(36) NOT NULL PRIMARY KEY, name VARCHAR(16), gold VARCHAR(16), rank VARCHAR(20), visibility VARCHAR(20)");
        this.createTable("statsSW", "uuid VARCHAR(36) NOT NULL PRIMARY KEY, name VARCHAR(16), kills VARCHAR(20), wins VARCHAR(20), mortes VARCHAR(20)");
    }

    public void shutdown() {
        this.close();
    }

    public boolean isInitiated() {
        return this.hikariDataSource != null;
    }

    public void close() {
        if (this.hikariDataSource != null && !this.hikariDataSource.isClosed()) {
            this.hikariDataSource.close();
        }

    }

    public Connection getConnection() throws SQLException {
        if (this.hikariDataSource == null) {
            throw new SQLException("DataSource is not initialized");
        } else {
            return this.hikariDataSource.getConnection();
        }
    }

    public void createTable(String name, String info) {
        (new Thread(() -> {
            try (Connection resource = this.getConnection()) {
                PreparedStatement statement = resource.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + "(" + info + ")");
                Throwable var6 = null;

                try {
                    statement.execute();
                } catch (Throwable var31) {
                    var6 = var31;
                    throw var31;
                } finally {
                    if (statement != null) {
                        if (var6 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var30) {
                                var6.addSuppressed(var30);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }
            } catch (SQLException exception) {
                Bukkit.getConsoleSender().sendMessage("An error occurred while creating database table " + name + ".");
                exception.printStackTrace();
            }

        })).start();
    }

    public boolean execute(String query, Object... values) {
        (new Thread(() -> {
            try (Connection resource = this.getConnection()) {
                PreparedStatement statement = resource.prepareStatement(query);
                Throwable var6 = null;

                try {
                    for(int i = 0; i < values.length; ++i) {
                        statement.setObject(i + 1, values[i]);
                    }

                    statement.execute();
                } catch (Throwable var31) {
                    var6 = var31;
                    throw var31;
                } finally {
                    if (statement != null) {
                        if (var6 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var30) {
                                var6.addSuppressed(var30);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }
            } catch (SQLException exception) {
                Bukkit.getConsoleSender().sendMessage("An error occurred while executing an update on the database.");
                Bukkit.getConsoleSender().sendMessage("MySQL#execute : " + query);
                exception.printStackTrace();
            }

        })).start();
        return true;
    }

    public void select(String query, SelectCall callback, Object... values) {
        (new Thread(() -> {
            try (Connection resource = this.getConnection()) {
                PreparedStatement statement = resource.prepareStatement(query);
                Throwable var7 = null;

                try {
                    for(int i = 0; i < values.length; ++i) {
                        statement.setObject(i + 1, values[i]);
                    }

                    callback.call(statement.executeQuery());
                } catch (Throwable var32) {
                    var7 = var32;
                    throw var32;
                } finally {
                    if (statement != null) {
                        if (var7 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var31) {
                                var7.addSuppressed(var31);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }
            } catch (SQLException exception) {
                Bukkit.getConsoleSender().sendMessage("An error occurred while executing a query on the database.");
                Bukkit.getConsoleSender().sendMessage("MySQL#select : " + query);
                exception.printStackTrace();
            }

        })).start();
    }
}
