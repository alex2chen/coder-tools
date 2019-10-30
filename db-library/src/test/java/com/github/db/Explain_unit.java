package com.github.db;

import com.github.db.mysql.MysqlJdbcTemplate;
import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author: alex.chen
 * @Description: 在大数据表中count(*)的速度一直提不上去，用explain来获取总行数
 * @Date: 2019/10/26
 */
public class Explain_unit extends MysqlJdbcTemplate {
    private String username = "pc-mid-p_test";
    private String password = "pc-mid-p_test@2019";
    private String url = "jdbc:mysql://10.251.76.11:3306/yh_srm_productcenter_test?autoReconnect=true&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai";
    private Function<ResultSet, String> defualtRSConsumer = rs -> {
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                if (!rs.next()) break;
                sb.append(rs.getObject("rows")).append(",");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    };

    @Test
    public void go_count() {
        String sql = "SELECT COUNT(*) FROM base_product_info_sap";
        explain(sql, stmt -> {
        }, rs -> {
            try {
                if (!rs.next()) return "";
                return "" + rs.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
                return "";
            }
        });
    }

    @Test
    public void go_getAll() {
        String sql = "EXPLAIN SELECT * FROM base_product_info_sap";
        explain(sql, stmt -> {
        }, defualtRSConsumer);
    }

    @Test
    public void go_getById() {
        String sql = "EXPLAIN SELECT * FROM base_product_info_sap WHERE id=?";
        explain(sql, stmt -> {
            try {//not data
                stmt.setLong(1, 2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, defualtRSConsumer);

        explain(sql, stmt -> {
            try {//PRIMARY - Unique
                stmt.setLong(1, 1000011056);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, defualtRSConsumer);
    }

    /**
     * what's problem?
     * 其实只有1个行数据，但分析后有2行数据，足以可见是预估而非精确
     */
    @Test
    public void go_twoIndex() {
        String sql = "EXPLAIN SELECT * FROM base_product_info_sap WHERE id=? OR product_code=?";
        explain(sql, stmt -> {
            try {
                /**
                 * id PRIMARY - Unique
                 * product_code PRIMARY - Unique
                 */
                stmt.setLong(1, 1000013165);
                stmt.setString(2, "754474");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, defualtRSConsumer);
    }

    private void explain(String sql, Consumer<PreparedStatement> paramConsumer, Function<ResultSet, String> rsConsumer) {
        execute(() -> getConnection(url, username, password), connection -> {
            try {
                Stopwatch stopwatch = Stopwatch.createStarted();
                PreparedStatement stmt = connection.prepareStatement(sql);
                paramConsumer.accept(stmt);
                ResultSet resultSet = stmt.executeQuery();
                String result = rsConsumer.apply(resultSet);
                resultSet.close();
                System.out.println(String.format("spends:%s,sql=[%s],result=%s", stopwatch, sql, result));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}
