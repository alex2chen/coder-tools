package com.github.common.io;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/7/10.
 */
public class CSVPrinter_test {
    @Test
    public void read() throws IOException {
        String file = "src/test/resources/1.csv";
        String filePath = new File(file).getAbsolutePath();
        CSVParser parser = CSVUtils.getCSVParser(filePath);
        Iterator<CSVRecord> iterator = parser.iterator();
        while (iterator.hasNext()) {
            CSVRecord record = iterator.next();
            System.out.println(Arrays.toString(Iterators.toArray(record.iterator(), Object.class)));
            System.out.println(record);
        }
        IOUtils.closeQuietly(parser);
    }

    @Test
    public void write() {
        long initMem = Runtime.getRuntime().freeMemory() / 1024L / 1024L;
        Stopwatch stopwatch = Stopwatch.createStarted();
        String file = "target/student_tmp.csv";
        try {
            CSVPrinter printer = CSVUtils.getCSVPrinter(file);
            printer.printRecord("id", "age");//header
            List<String> rows = Lists.newArrayList();
            rows.add("02");
            rows.add("1832432432424");
            printer.printRecord(rows);
            printer.print(null);
            printer.print("343w2");
            IOUtils.closeQuietly(printer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endMem = Runtime.getRuntime().freeMemory() / 1024L / 1024L;
        System.out.println("ok." + stopwatch + ",spend:" + (initMem - endMem) + "MB");
    }

    @Test
    public void writeBcp() throws IOException {
        //Create bcp file if not exist
        File bcpFile = new File("target/test.csv");
        //bcpFile.delete();
        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        //boolean newFile = false;
        FileOutputStream bcpFileWriter = new FileOutputStream(bcpFile);
        bcpFileWriter.write(bom);
        //bcpFile.delete();
        String title = "\"MD5\",\"扫描文件名\",\"扫描时间\"," +
                "\"是否病毒\",\"安全等级\",\"病毒英文名称\"," +
                "\"病毒变种\",\"病毒类型\",\"病毒影响\"," +
                "\"感染系统\",\"传播方式\",\"备注\"";
        bcpFileWriter.write((new String(title.getBytes(), "utf-8")).getBytes());
        bcpFileWriter.write("\n".getBytes());

        String appStr = "\"" + 123 + "\","
                + "\"" + 123 + "\","
                + 03333 + ","
                + 432342453 + ","
                + 123 + ","
                + "\"" + 123 + "\","
                + "\"\","
                + 123 + ","
                + "\"" + 123 + "\","
                + "\"" + 123 + "\","
                + "\"" + 123 + "\","
                + "\"" + 123 + "\"\n";
        bcpFileWriter.write(appStr.getBytes());
        bcpFileWriter.close();
    }

}
