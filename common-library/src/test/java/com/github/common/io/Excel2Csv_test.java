package com.github.common.io;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/12/13.
 */
public class Excel2Csv_test {
    @Test
    public void run_XLS2CSV() throws InterruptedException {
        System.out.println("xls to csv");
        Stopwatch stopwatch = Stopwatch.createStarted();
        XLS2CSV xls2csv = null;
        String file = "广州数据.xlsx";
        try {
            ClassPathResource pathResource = new ClassPathResource(file);
            String inputFile = pathResource.getFile().getPath();
            System.out.println(inputFile);
            xls2csv = new XLS2CSV(inputFile, "target/XLS2CSV-" + file + ".csv");
            xls2csv.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(stopwatch);
    }

    @Test
    public void run_XLSX2CSV() throws IOException {
        System.out.println("xlsx to csv");
        Stopwatch stopwatch = Stopwatch.createStarted();
        XLSX2CSV xlsx2csv = null;
        String file = "广州数据.xlsx";
        try {
            ClassPathResource pathResource = new ClassPathResource(file);
            String inputFile = pathResource.getFile().getPath();
            xlsx2csv = new XLSX2CSV(inputFile, "target/XLSX2CSV-" + file + ".csv");
            xlsx2csv.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ok." + stopwatch);
    }

}
