package com.github.jvm.util.i18n;

import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @Author: alex
 * @Description: 国际化(internationalization)又称为 i18n(读法为i 18 n，
 * 据说是因为internationalization(国际化)这个单词从i到n之间有18个英文字母) 一个资源包中的所有资源文件的关键字必须相同，值则为相应国家的文字。
 * 并且资源文件中采用的是properties格式文件，所以文件中的所有字符都必须是ASCII字码，
 * 属性(properties)文件是不能保存中文的，对于像中文这样的非ACSII字符，须先进行编码。
 * native2ascII
 * 用于将中文字符进行编码处理;
 * jdk\bin\native2ascii.exe;
 * $native2ascii -encoding GBK zh.txt i_nv.txt：  将zh.txt转换为本地编码，输出到i_nv.txt
 * $native2ascii -reverse -encoding GBK i.txt i_gbk.txt：将i.txt转换为GBK编码，输出到i_gbk.txt
 * @Date: created in 2016/6/8.
 */
public class ResourceBundle_test {
    @Test
    public void read() {
        //资源包基命
        String basename = "member";
        Locale cn = Locale.CANADA;
        Locale us = Locale.US;
        ResourceBundle resourceBundleCN = ResourceBundle.getBundle(basename, cn);
        for (String item : resourceBundleCN.keySet()) {
            System.out.println(resourceBundleCN.getString(item));
        }
        ResourceBundle resourceBundleUS = ResourceBundle.getBundle(basename, us);
        for (String item : resourceBundleUS.keySet()) {
            System.out.println(resourceBundleUS.getString(item));
        }
    }
}
