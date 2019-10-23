package com.github.common.net;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;
import org.assertj.core.util.Strings;
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
public class Ftp_test {
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


    public static class FTPTemplate {
        private FTPClient ftpClient;
        private FTPClientAdmin admin;


        public void setFtpClient(FTPClient ftpClient) {
            this.ftpClient = ftpClient;
        }

        public void setAdmin(FTPClientAdmin admin) {
            this.admin = admin;
        }

        public static FTPClientBuilder builder() {
            return new FTPClientBuilder();
        }

        public String[] listName() {
            try {
                checkConnected();
                return ftpClient.listNames();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public List<FTPFile> listFile(String remotePath) {
            try {
                checkConnected();
                return Arrays.asList(ftpClient.listFiles(remotePath));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public List<FTPFile> listFile(String remotePath, int top) {
            try {
                checkConnected();
                FTPListParseEngine engine = ftpClient.initiateListParsing(remotePath);
                return Arrays.asList(engine.getNext(top));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public boolean setWorkingDirectory(String dir) {
            if (Strings.isNullOrEmpty(dir)) {
                return false;
            }
            try {
                checkConnected();
                return ftpClient.changeWorkingDirectory(dir);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void checkConnected() throws IOException {
            if (ftpClient.isConnected()) {
                int replyCode = ftpClient.getReplyCode();
                if ((!FTPReply.isPositiveCompletion(replyCode))) {
                    disconnect();
                } else {
                    return;
                }
            }
            ftpClient.connect(this.admin.ip, this.admin.port);
            ftpClient.login(this.admin.username, this.admin.password);
        }

        public boolean makeDir(String dir) {
            if (Strings.isNullOrEmpty(dir)) {
                return false;
            }
            try {
                checkConnected();
                return ftpClient.makeDirectory(dir);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void disconnect() {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        public void uploadFile(String localFile, String targetFile) {
            try {
                checkConnected();
                File file = new File(localFile);
                ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                FileInputStream localFileStream = new FileInputStream(file);
                ftpClient.storeFile(targetFile, localFileStream);
                IOUtils.closeQuietly(localFileStream);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void uploadFile(InputStream localFileStream, String targetFile) {
            try {
                checkConnected();
                ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                ftpClient.storeFile(targetFile, localFileStream);
                IOUtils.closeQuietly(localFileStream);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public InputStream downloadFile(String remoteFile) {
            try {
                checkConnected();
                return ftpClient.retrieveFileStream(remoteFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void downloadFile(String remoteFile, String destFile) {
            File downloadFile = new File(destFile);
            File parentDir = downloadFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdir();
            }
            OutputStream outputStream = null;
            try {
                checkConnected();
                outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                boolean status = ftpClient.retrieveFile(remoteFile, outputStream);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Data
    @AllArgsConstructor
    public static class FTPClientAdmin {
        private String ip;
        private Integer port = 21;
        private String username;
        private String password;
    }

    public static class FTPClientBuilder {
        private Integer connectTimeout = 30 * 1000;
        private Boolean useCompressedTransfer = false;
        private FTPClientAdmin admin;
        private Boolean autoLogin = false;

        public FTPClientBuilder connectTimeout(int seconds) {
            connectTimeout = seconds * 1000;
            return this;
        }

        public FTPClientBuilder useCompressedTransfer() {
            useCompressedTransfer = true;
            return this;
        }

        public FTPClientBuilder admin(String ip, Integer port, String username, String password) {
            admin = new FTPClientAdmin(ip, port, username, password);
            return this;
        }

        public FTPClientBuilder autoLogin() {
            this.autoLogin = true;
            return this;
        }

        public FTPTemplate build() {
            try {
                FTPTemplate ftpTemplate = new FTPTemplate();
                ftpTemplate.setAdmin(this.admin);
                FTPClient ftpClient = new FTPClient();
                //ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //ftpClient.setControlEncoding("utf-8");
                // 设置被动模式
                //ftpClient.enterLocalPassiveMode();
                ftpClient.setConnectTimeout(this.connectTimeout);
                if (useCompressedTransfer) {
                    ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.COMPRESSED_TRANSFER_MODE);
                }
                if (autoLogin) {
                    ftpClient.connect(this.admin.ip, this.admin.port);
                    if (!ftpClient.login(this.admin.username, this.admin.password)) {
                        throw new RuntimeException("登录ftp失败.");
                    }
                }
                ftpTemplate.setFtpClient(ftpClient);
                return ftpTemplate;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
