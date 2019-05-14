package com.github.common.compress.gzip;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/7/10.
 */
public class GzipApache {

    private GzipApache() {}

    public static void compressGZIP(File input, File output) throws IOException {
        try (GzipCompressorOutputStream out = new GzipCompressorOutputStream(new FileOutputStream(output))){
            IOUtils.copy(new FileInputStream(input), out);
        }
    }

    public static void decompressGZIP(File input, File output) throws IOException {
        try (GzipCompressorInputStream in = new GzipCompressorInputStream(new FileInputStream(input))){
            IOUtils.copy(in, new FileOutputStream(output));
        }
    }
}