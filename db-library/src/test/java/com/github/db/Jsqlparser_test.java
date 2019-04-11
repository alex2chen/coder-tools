package com.github.db;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import org.junit.Test;

import java.io.StringReader;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/11.
 */
public class Jsqlparser_test {
    @Test
    public void hint_run() {
        String sql = "SELECT * FROM export_request FORCE INDEX (idx_file_name) WHERE id = ? AND file_name='xxx'";
        sqlParse(sql);
    }

    @Test
    public void joinParse_run() {
        String sql = "SELECT\n" +
                "  a.loading_id,\n" +
                "  a.pudplan_id,\n" +
                "  a.loading_no,\n" +
                "  a.create_time,\n" +
                "  a.status,\n" +
                "  a.app_status,\n" +
                "  a.tran_line,\n" +
                "  a.vehicle_no,\n" +
                "  a.register,\n" +
                "  a.car_owner_name,\n" +
                "  a.car_owner_phone,\n" +
                "  a.driver_name,\n" +
                "  a.driver_phone,\n" +
                "  a.waybill_number,\n" +
                "  a.total_weight,\n" +
                "  a.total_volume,\n" +
                "  a.account_type,\n" +
                "  a.current_fee,\n" +
                "  a.arrive_fee,\n" +
                "  a.return_fee,\n" +
                "  a.total_fee,\n" +
                "  a.remark,\n" +
                "  a.departure_city_name,\n" +
                "  a.arrive_city_name,\n" +
                "  a.departure_point_name,\n" +
                "  a.arrive_point_name,\n" +
                "  a.vehicle_license,\n" +
                "  a.vehicle_type,\n" +
                "  a.driver_license,\n" +
                "  a.total_number,\n" +
                "  a.is_crh,\n" +
                "  (SELECT\n" +
                "     MIN(c.load_aging_time)\n" +
                "   FROM tms_loading_details b\n" +
                "     LEFT JOIN tms_waybill_current c\n" +
                "       ON b.waybill_id = c.waybill_id\n" +
                "   WHERE b.loading_id = a.loading_id\n" +
                "       AND b.delete_flag = 0) AS load_aging_time,\n" +
                "  ''                     AS load_aging_time_diff,\n" +
                "  ''                     AS load_aging_diff_percent,\n" +
                "  (SELECT\n" +
                "     MIN(c.send_aging_time)\n" +
                "   FROM tms_loading_details b\n" +
                "     LEFT JOIN tms_waybill_current c\n" +
                "       ON b.waybill_id = c.waybill_id\n" +
                "   WHERE b.loading_id = a.loading_id\n" +
                "       AND b.delete_flag = 0) AS send_aging_time,\n" +
                "  ''                     AS send_aging_time_diff,\n" +
                "  ''                     AS send_aging_diff_percent,\n" +
                "  (SELECT\n" +
                "     MIN(c.sigin_aging_time)\n" +
                "   FROM tms_loading_details b\n" +
                "     LEFT JOIN tms_waybill_current c\n" +
                "       ON b.waybill_id = c.waybill_id\n" +
                "   WHERE b.loading_id = a.loading_id\n" +
                "       AND b.delete_flag = 0) AS sigin_aging_time,\n" +
                "  ''                     AS sigin_aging_time_diff,\n" +
                "  ''                     AS sigin_aging_diff_percent,\n" +
                "  0                      AS report_nums,\n" +
                "  ''                     AS report_keys\n" +
                "FROM tms_loading a\n" +
                "WHERE a.delete_flag = 0\n" +
                "ORDER BY a.status ASC,a.create_time DESC\n" +
                "LIMIT 23 OFFSET 2";
        sqlParse(sql);
    }

    private void sqlParse(String sql) {
        Statement var2 = null;
        try {
            var2 = (new CCJSqlParserManager()).parse(new StringReader(sql));
            System.out.println(var2);
        } catch (JSQLParserException var4) {
            var4.printStackTrace();
        }
    }
}
