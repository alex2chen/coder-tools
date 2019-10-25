package com.github.common.net;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/23
 */
public class Ftp_unit {
    private FTPTemplate ftpTemplate;
    private String filePath = "/sap_file/RME_ECC003";

    @Before
    public void init() {
        ftpTemplate = FTPTemplate.builder().admin("10.251.64.121", 21, "ftp_sap_qas", "ftp_sap_qas@2019").autoLogin().build();
    }

    @Test
    public void go_listName() {
        ftpTemplate.setWorkingDirectory(filePath);
        System.out.println(Arrays.toString(ftpTemplate.listName()));
        //ftpTemplate.disconnect();
    }

    @Test
    public void go_listFile() {
        List<FTPFile> ftpFiles = ftpTemplate.listFile(filePath);
        ftpFiles.forEach(System.out::println);
    }

    @Test
    public void go_listFile2() {
        List<FTPFile> ftpFiles = ftpTemplate.listFile(filePath,2);
        ftpFiles.forEach(System.out::println);
    }

    @Test
    public void go_download() throws IOException {
        String remoteFile = "/sap_file/RME_ECC003/20191018-151852-754_RME_ECC003_d24cf6a3b3004d59996db9f3facc92de.xml";
        InputStream input = ftpTemplate.downloadFile(remoteFile);
        if (input != null) {
            System.out.println(IOUtils.toString(input, Charset.defaultCharset()));
        }
        //ftpTemplate.disconnect();
    }

    @Test
    public void go_download2() throws IOException {
        String remoteFile = "/sap_file/RME_ECC003/20191018-151852-754_RME_ECC003_d24cf6a3b3004d59996db9f3facc92de.xml";
        String targetFile = "target/demo.xml";
        ftpTemplate.downloadFile(remoteFile, targetFile);
        //ftpTemplate.disconnect();
    }

    @Test
    public void go_upload() throws IOException {
        String localFile = "target/demo.xml";
        String targetFile = "/sap_file/RME_ECC003/demo.xml";
        ftpTemplate.uploadFile(localFile, targetFile);
        //ftpTemplate.disconnect();
    }
}
