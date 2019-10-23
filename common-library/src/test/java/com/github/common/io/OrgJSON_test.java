package com.github.common.io;

import org.json.JSONObject;
import org.json.XML;
import org.junit.Test;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/23
 */
public class OrgJSON_test {
    @Test
    public void go_xmlToJSONObject(){
        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<note>\n" +
                "<to>George</to>\n" +
                "<from>John</from>\n" +
                "<heading>Reminder</heading>\n" +
                "<body>Don't forget the meeting!</body>\n" +
                "</note>";
        JSONObject jsonObject = XML.toJSONObject(xml);
        System.out.println(jsonObject);
    }
}
