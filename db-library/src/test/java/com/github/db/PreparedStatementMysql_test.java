package com.github.db;

import com.github.db.mysql.MysqlJdbcTemplate;
import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/4/11.
 */
public class PreparedStatementMysql_test extends MysqlJdbcTemplate {
    private String username = "root";
    private String password = "123456";
    private String url = "jdbc:mysql://localhost:3306/batch";
    private String sql = "SELECT * FROM export_request WHERE id = ?";
    private String sql2 = "SELECT * FROM export_request FORCE INDEX (idx_file_name) WHERE id = ? AND file_name='xxx'";
    private int maxTimes = 100000;

    @Test
    public void simple_run() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        execute(() -> getConnection(url, username, password), connection -> {
            try {
                for (int i = 0; i < maxTimes; i++) {
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setLong(1, Math.abs(new Random().nextLong()));
                    // execute
                    stmt.executeQuery();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        System.out.println("useServerPrepStmtsAndcachePrepStmts_run:" + stopwatch);
    }

    @Test
    public void useServerPrepStmts_run() throws SQLException, ClassNotFoundException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        execute(() -> getConnection(url + "?useServerPrepStmts=true", username, password), connection -> {
            try {
                for (int i = 0; i < maxTimes; i++) {
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setLong(1, Math.abs(new Random().nextLong()));
                    // execute
                    stmt.executeQuery();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        System.out.println("useServerPrepStmtsAndcachePrepStmts_run:" + stopwatch);
    }

    @Test
    public void useServerPrepStmtsAndcachePrepStmts_run() throws SQLException, ClassNotFoundException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        execute(() -> getConnection(url + "?useServerPrepStmts=true&cachePrepStmts=true", username, password), connection -> {
            try {
                for (int i = 0; i < maxTimes; i++) {
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setLong(1, Math.abs(new Random().nextLong()));
                    // execute
                    stmt.executeQuery();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        System.out.println("useServerPrepStmtsAndcachePrepStmts_run:" + stopwatch);
    }
}
