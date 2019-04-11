package com.github.common.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/7/10.
 */
public class CSVUtils {
    public static CSVParser getCSVParser(String filePath) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
        return new CSVParser(isr, format);
    }

    public static CSVPrinter getCSVPrinter(String filePath) throws IOException {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD_CSV.withRecordSeparator("\n");
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
        return new CSVPrinter(osw, format);
    }
}
