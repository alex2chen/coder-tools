package com.github.common.io;

import com.google.common.base.Stopwatch;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/1/11.
 */
public class IOUtils_test {
    private String file = "1.csv";
    @Test
    public void readLine() {
        int result = 0;
        InputStream inputStream = null;
        try {
            ClassPathResource pathResource = new ClassPathResource(file);
            inputStream = pathResource.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charsets.UTF_8);
            BufferedReader bufferedReader = IOUtils.toBufferedReader(streamReader);
            while (bufferedReader.readLine() != null) result++;
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) IOUtils.closeQuietly(inputStream);
        }
    }
}
