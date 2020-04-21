package com.github.code.generate.dao;

import com.google.common.collect.*;
import com.github.code.generate.core.ConstantVelocity;
import com.github.code.generate.domain.GeneratorConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fei.chen on 2018/6/28.
 */
public class DBManager {

    public static Connection getSQLConnection(GeneratorConfig generatorConfig) throws Exception {
        Connection connection = null;
        try {
            Class.forName(generatorConfig.getDbDriver());
            connection = DriverManager.getConnection(generatorConfig.getDbUrl(), generatorConfig.getDbUer(), generatorConfig.getDbPass());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return connection;
    }

    public static List<String> listMYSQLAllTables(Connection conn) throws Exception {
        List<String> alltables = Lists.newArrayList();
        PreparedStatement pstmt;
        ResultSet rs;
        pstmt = conn.prepareStatement("show tables");
        rs = pstmt.executeQuery();
        String tablename = null;
        while (rs.next()) {
            tablename = rs.getString(1);
            alltables.add(tablename);
        }
        pstmt.close();
        return alltables;
    }

    public static List<String> listMYSQLComments(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData dmd = conn.getMetaData();
        ResultSet rs = dmd.getColumns(null, null, tableName, null);
        List<String> commentList = new ArrayList<String>();
        while (rs.next()) {
            commentList.add(rs.getString("REMARKS"));
        }
        return commentList;
    }

    public static ResultSetMetaData getFields(Connection conn, String tableName) {
        String sql = "select * from " + tableName + " limit 1";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rsss = pstmt.executeQuery();
            ResultSetMetaData rsmd = rsss.getMetaData();
            pstmt.close();
            return rsmd;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getMYSQLPkField(Connection conn, String tableName) throws SQLException {
        PreparedStatement pstmt;
        String pkField = null;
        pstmt = conn.prepareStatement("show index from " + tableName + "  where Key_name='PRIMARY'");
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            pkField = rs.getString(5);
            if (pkField.indexOf(",") > 0) {
                pkField = pkField.substring(0, pkField.indexOf(","));

            }
        }
        pstmt.close();
        return pkField;
    }

    public Multimap<String, String> getMYSQLIndexField(Connection conn, String tableName) throws SQLException {
        Multimap<String, String> result = ArrayListMultimap.create();
        PreparedStatement pstmt;
        String pkField = null;
        pstmt = conn.prepareStatement("show index from " + tableName);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            String nonUnique = rs.getString(2);
            String keyName = rs.getString(3);
            String columnName = rs.getString(5);
            if (keyName.equals("PRIMARY")) {//主键
                result.put(ConstantVelocity.PK_ID, columnName);
            } else {//普通索引和唯一索引
                if (nonUnique.equals("0")) {//唯一索引
                    result.put(ConstantVelocity.Unique_index, columnName);
                }
                if (nonUnique.equals("1")) {//普通索引
                    result.put(ConstantVelocity.Common_index, columnName);
                }
            }
        }
        pstmt.close();
        return result;
    }

    @Deprecated
    public static String getMSSQLPkField(Connection conn, String tableName) throws SQLException {
        PreparedStatement pstmt;
        String pkField = null;
        pstmt = conn.prepareStatement("SELECT syscolumns.name From sysobjects inner join syscolumns on sysobjects.id = syscolumns.id left outer join (select  o.name sTableName, c.Name sColName From  sysobjects o  inner join sysindexes i on o.id = i.id and (i.status & 0X800) = 0X800 inner join syscolumns c1 on c1.colid <= i.keycnt and c1.id = o.id inner join syscolumns c on o.id = c.id and c.name = index_col (o.name, i.indid, c1.colid)) pkElements on pkElements.sTableName = sysobjects.name and pkElements.sColName = syscolumns.name inner join sysobjects syscons on sysobjects.id=syscons.parent_obj and syscons.xtype='PK' where (syscolumns.Status & 128)=128 and sysobjects.name=?");
        pstmt.setString(1, tableName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            pkField = rs.getString(1);
        }
        pstmt.close();
        return pkField;
    }

    @Deprecated
    public static List<String> listMSSQLAllTables(Connection connection) throws Exception {
        List<String> alltables = Lists.newArrayList();
        ResultSet rs;
        PreparedStatement pstmt = connection.prepareStatement("select [name] from sysobjects where xtype='U'");
        rs = pstmt.executeQuery();
        String tablename = null;
        while (rs.next()) {
            tablename = rs.getString(1);
            alltables.add(tablename);
        }
        return alltables;
    }

    public static void close(Connection connection) {
        try {
            if (connection != null) connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
