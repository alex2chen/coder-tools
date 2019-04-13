package com.github.common.io.compress;

import com.github.common.io.compress.jar.JAR;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @Author: alex
 * @Description: 打JAR包
 * @Date: created in 2017/7/10.
 */
public class JAR_unit {

    private static final String OUTPUT_DIRECTORY = new File("tmp").mkdir() ? "tmp" : "";
    private static final String TAR_GZIP_SUFFIX = ".jar";

    private static final String MULTIPLE_RESOURCES = "/example-multiple-resources";
    private static final String RECURSIVE_DIRECTORY = "/example-recursive-directory";

    private static final String MULTIPLE_RESOURCES_PATH = OUTPUT_DIRECTORY + MULTIPLE_RESOURCES + TAR_GZIP_SUFFIX;
    private static final String RECURSIVE_DIRECTORY_PATH = OUTPUT_DIRECTORY + RECURSIVE_DIRECTORY + TAR_GZIP_SUFFIX;

    @Test
    public void run() throws IOException {
        Class clazz = JAR_unit.class;
        // get multiple resources files to compress
        File resource1 = new File(clazz.getResource("/in/resource1.txt").getFile());
        File resource2 = new File(clazz.getResource("/in/resource2.txt").getFile());
        File resource3 = new File(clazz.getResource("/in/resource3.txt").getFile());

        // compress multiple resources
        JAR.compress(MULTIPLE_RESOURCES_PATH, resource1, resource2, resource3);

        // decompress multiple resources
        JAR.decompress(MULTIPLE_RESOURCES_PATH, new File(OUTPUT_DIRECTORY + MULTIPLE_RESOURCES));

        // get directory file to compress
        File directory = new File(clazz.getResource("/in/dir").getFile());

        // compress recursive directory
        JAR.compress(RECURSIVE_DIRECTORY_PATH, directory);

        // decompress recursive directory
        JAR.decompress(RECURSIVE_DIRECTORY_PATH, new File(OUTPUT_DIRECTORY + RECURSIVE_DIRECTORY));
    }
}