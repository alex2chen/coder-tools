package com.github.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

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
            e.printStackTrace();
        }
    }

    private Connection getConnection(String url) throws SQLException {
        String username = "root";
        String password = "123456";
        return DriverManager.getConnection(url, username, password);
    }

    public void execute(String url, Consumer<Connection> consumer) {
        execute(url, consumer, true);
    }

    public void execute(String url, Consumer<Connection> consumer, boolean isClose) {
        try {
            Connection connection = getConnection(url);
            consumer.accept(connection);
            if (isClose) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
