package com.github.common.io;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * @Author: alex
 * @Description: 有一个很大的问题就是，新建一个文件默认是换行的，LineNumberReader认为是1行
 * @Date: created in 2018/4/11.
 */
public class LineNumberReader_test {
    @Test
    public void readerLine() throws IOException {
        int result = 0;
        ClassPathResource pathResource = new ClassPathResource("1.csv");
        System.out.println(pathResource.getPath());
        File file = pathResource.getFile();
        System.out.println(file.length());
        System.out.println(file);
        LineNumberReader reader = new LineNumberReader(new FileReader(file));
        reader.skip(file.length());
        result = reader.getLineNumber() + 1;
        System.out.println(result);
        IOUtils.closeQuietly(reader);
    }
}
