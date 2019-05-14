package com.github.common.compress;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/7/10.
 */
public class SevenZ_unit {

    private static final String OUTPUT_DIRECTORY = new File("tmp").mkdir() ? "tmp" : "";
    private static final String TAR_GZIP_SUFFIX = ".7z";

    private static final String MULTIPLE_RESOURCES = "/example-multiple-resources";
    private static final String RECURSIVE_DIRECTORY = "/example-recursive-directory";

    private static final String MULTIPLE_RESOURCES_PATH = OUTPUT_DIRECTORY + MULTIPLE_RESOURCES + TAR_GZIP_SUFFIX;
    private static final String RECURSIVE_DIRECTORY_PATH = OUTPUT_DIRECTORY + RECURSIVE_DIRECTORY + TAR_GZIP_SUFFIX;

    @Test
    public void run() throws IOException {
        Class clazz = SevenZ_unit.class;
        // get multiple resources files to compress
        File resource1 = new File(clazz.getResource("/in/resource1.txt").getFile());
        File resource2 = new File(clazz.getResource("/in/resource2.txt").getFile());
        File resource3 = new File(clazz.getResource("/in/resource3.txt").getFile());
        // compress multiple resources
        com.github.common.compress.SevenZ.compress(MULTIPLE_RESOURCES_PATH, resource1, resource2, resource3);
        // decompress multiple resources
        com.github.common.compress.SevenZ.decompress(MULTIPLE_RESOURCES_PATH, new File(OUTPUT_DIRECTORY + MULTIPLE_RESOURCES));
        // get directory file to compress
        File directory = new File(clazz.getResource("/in/dir").getFile());
        // compress recursive directory
        com.github.common.compress.SevenZ.compress(RECURSIVE_DIRECTORY_PATH, directory);
        // decompress recursive directory
        SevenZ.decompress(RECURSIVE_DIRECTORY_PATH, new File(OUTPUT_DIRECTORY + RECURSIVE_DIRECTORY));
    }
}