package com.github.common.io;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/12/10.
 */
public class QRCodeGenerator {
    private QrCodeConfig qrCodeConfig;

    public QRCodeGenerator(QrCodeConfig qrCodeConfig) {
        this.qrCodeConfig = qrCodeConfig;
    }

    public static QrCodeBuilder builder() {
        return new QrCodeBuilder();
    }

    /**
     * 生成base64二维码
     *
     * @param content
     * @return
     * @throws Exception
     */
    public byte[] generateQrCode(String content) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.ERROR_CORRECTION, qrCodeConfig.getErrorCorrectionLevel());
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, qrCodeConfig.getMargin());
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrCodeConfig.getWidth(), qrCodeConfig.getHeight(), hints);
            BufferedImage image = new BufferedImage(qrCodeConfig.getWidth(), qrCodeConfig.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < qrCodeConfig.getWidth(); x++) {
                for (int y = 0; y < qrCodeConfig.getHeight(); y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? qrCodeConfig.getForeColor() : qrCodeConfig.getBackgroudColor());
                }
            }
            if (qrCodeConfig.getLogoFile() != null && qrCodeConfig.getLogoFile().exists()) {//加载logo
                Graphics2D gs = image.createGraphics();
                int ratioWidth = image.getWidth() * 2 / 10;
                int ratioHeight = image.getHeight() * 2 / 10;
                Image img = ImageIO.read(qrCodeConfig.getLogoFile());
                int logoWidth = img.getWidth(null) > ratioWidth ? ratioWidth : img.getWidth(null);
                int logoHeight = img.getHeight(null) > ratioHeight ? ratioHeight : img.getHeight(null);
                int x = (image.getWidth() - logoWidth) / 2;
                int y = (image.getHeight() - logoHeight) / 2;
                gs.drawImage(img, x, y, logoWidth, logoHeight, null);
                gs.setColor(Color.black);
                gs.setBackground(Color.WHITE);
                gs.dispose();
                img.flush();
            }
            ImageIO.write(image, "JPG", out);
            // MatrixToImageWriter.writeToPath(bitMatrix, suffix, imageFile.toPath(), config);
            return out.toByteArray();
        }
    }

    public String generateQrCodeToBase64(String content) throws Exception {
        return Base64.encodeBase64String(generateQrCode(content));
    }


    public static class QrCodeBuilder {
        private QrCodeConfig qrCodeConfig;

        public QrCodeBuilder() {
            qrCodeConfig = new QrCodeConfig();
        }

        public QrCodeBuilder size(int width, int height) {
            qrCodeConfig.setWidth(width);
            qrCodeConfig.setHeight(height);
            return this;
        }

        public QrCodeBuilder foreColor(int foreColor) {
            qrCodeConfig.setForeColor(foreColor);
            return this;
        }

        public QrCodeBuilder backgroudColor(int backgroudColor) {
            qrCodeConfig.setBackgroudColor(backgroudColor);
            return this;
        }

        public QrCodeBuilder logoPath(String logoFile) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(logoFile), "logoFile is required.");
            try {
                File file = new ClassPathResource(logoFile).getFile();
                qrCodeConfig.setLogoFile(file);
            } catch (IOException ex) {
                throw new RuntimeException("logoFile is illegal:" + ex.getMessage());
            }
            return this;
        }

        public QRCodeGenerator build() {
            return new QRCodeGenerator(qrCodeConfig);
        }
    }

    public static class QrCodeConfig {
        private int width = 300;              //二维码宽度
        private int height = 300;             //二维码高度
        private int foreColor = 0xFF000000;     //前景色
        private int backgroudColor = 0xFFFFFFFF;    //背景色
        private int margin = 1;               //白边大小，取值范围0~4
        private ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.H;// 指定纠错等级
        private File logoFile;//生成带logo需要

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getForeColor() {
            return foreColor;
        }

        public void setForeColor(int foreColor) {
            this.foreColor = foreColor;
        }

        public int getBackgroudColor() {
            return backgroudColor;
        }

        public void setBackgroudColor(int backgroudColor) {
            this.backgroudColor = backgroudColor;
        }

        public int getMargin() {
            return margin;
        }

        public void setMargin(int margin) {
            this.margin = margin;
        }

        public ErrorCorrectionLevel getErrorCorrectionLevel() {
            return errorCorrectionLevel;
        }

        public void setErrorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
            this.errorCorrectionLevel = errorCorrectionLevel;
        }

        public File getLogoFile() {
            return logoFile;
        }

        public void setLogoFile(File logoFile) {
            this.logoFile = logoFile;
        }
    }
}
