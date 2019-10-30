package com.github.db;

import com.github.db.mysql.MysqlJdbcTemplate;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: alex
 * @Description: Connection的三个方法与事务有关：
 * setAutoCommit（boolean）:设置是否为自动提交事务，
 * 如果true（默认值为true）表示自动提交，也就是每条执行的SQL语句都是一个单独的事务，
 * 如果设置为false，那么相当于开启了事务了；con.setAutoCommit(false) 表示开启事务。
 * commit（）：提交结束事务。
 * rollback（）：回滚结束事务。
 * @Date: created in 2017/4/11.
 */
public class XALockMysql_test extends MysqlJdbcTemplate {
    private String username = "root";
    private String password = "123456";
    private String url = "jdbc:mysql://localhost:3306/batch";
    private String sqlSelect = "SELECT * FROM employee WHERE empid=?";
    private String sqlSelectXL = "SELECT * FROM employee WHERE empid=? for UPDATE";
    private String sqlSelectSL = "SELECT * FROM employee WHERE empid=? lock IN  share mode";
    private String sqlUpdate = "UPDATE employee SET first_name=? WHERE empid=?";

    private void update(Connection connection) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sqlUpdate);
        pstmt.setString(1, "alex-66");
        pstmt.setInt(2, 25);
        int update = pstmt.executeUpdate();
        System.out.println("update:" + update);
    }

    @Test
    public void xlock_run() throws InterruptedException {
        execute(getConnection(url, username, password), connection -> {
            try {
                connection.setAutoCommit(false);//开启事物，for UPDATE一定要开启，否则自动就提交了（那样就没意义了）
                PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectXL);
                preparedStatement.setInt(1, 25);
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println(resultSet.next());
                update(connection);
                //在一个连接中，如果不commit，意味着update其实就没有生效
                //connection.commit();
                //连接关闭就会强制关闭（释放的话），排他锁会释放的
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, false);
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void select() throws SQLException, InterruptedException {
        //普通读（多版本读）以及是否添加了事物，任何锁都不影响的
        execute(getConnection(url, username, password), connection -> {
            try {
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
                preparedStatement.setInt(1, 25);
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("ResultSet.next:" + resultSet.next());
                System.out.println("first_name:" + resultSet.getString("first_name"));

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, false);
        //Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void slock_run() throws InterruptedException {
        //与xlock（排他锁）不兼容
        execute(getConnection(url, username, password), connection -> {
            try {
                connection.setAutoCommit(false);//不要忘了
                PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectSL);
                preparedStatement.setInt(1, 25);
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("ResultSet.next:" + resultSet.next());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, false);
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void update_run() throws SQLException {
        //与xlock（排他锁）不兼容
        //与slock（共享锁）不兼容
        //注意：它与xlock的那个Connection，不是同一个，如果是同一个Connection可以更新，这点很重要，分布式锁就是这么设计的。
        execute(getConnection(url, username, password), connection -> {
            try {
                update(connection);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, false);
    }

}
