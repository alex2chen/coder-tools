package com.github.common.compress;

import com.github.common.compress.gzip.GzipApache;
import com.github.common.compress.gzip.GzipJava;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/7/10.
 */
public class Gzip_unit {
    @Test
    public void run() throws IOException {
        Class clazz = GzipApache.class;
        // create files to read and write
        File example = new File(clazz.getResource("/example.xml").getFile());
        File output = new File("tmp/example.xml.gz");
        File decompressed = new File("tmp/decompressed.xml");

        // Java GZIP example compression decompression
        GzipJava.compressGZIP(example, output);
        GzipJava.decompressGzip(output, decompressed);
        // Apache GZIP example compression decompression
        GzipApache.compressGZIP(example, output);
        GzipApache.decompressGZIP(output, decompressed);
    }
}