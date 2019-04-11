package com.github.common.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/4/11.
 */
public class FilenameUtils_test {

    @Test
    public void fileMeta() throws IOException {
        String file = "test.txt";
        ClassPathResource pathResource = new ClassPathResource(file);
        String inputFile = pathResource.getFile().getPath();
        System.out.println(pathResource.getPath());
        System.out.println(inputFile);

        System.out.println(FilenameUtils.getExtension(inputFile));
        System.out.println(FilenameUtils.getFullPath(inputFile));
        System.out.println(FilenameUtils.getBaseName(inputFile));
        System.out.println(FileUtils.getFile(inputFile).getParent());
    }
}
