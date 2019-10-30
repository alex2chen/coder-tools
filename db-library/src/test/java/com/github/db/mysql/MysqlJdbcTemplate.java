package com.github.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/4/11.
 */
public class MysqlJdbcTemplate {
    static {
        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected Connection getConnection(String url, String username, String password) {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(Supplier<Connection> connection, Consumer<Connection> consumer) {
        execute(connection.get(), consumer, true);
    }

    public void execute(Connection connection, Consumer<Connection> consumer, boolean isClose) {
        try {
            consumer.accept(connection);
            if (isClose) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
