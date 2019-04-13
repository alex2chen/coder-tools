package com.github.common.io;

import org.junit.Test;

/**
 * @Author: alex
 * @Description 二维码
 * @Date: created in 2019/4/11.
 */
public class Zxing_test {

    @Test
    public void genUrl() throws Exception {
        String loginUrl = "http://jiema.wwei.cn/";
        System.out.println(QRCodeGenerator.builder().build().generateQrCodeToBase64(loginUrl));
    }
/**
 * 对请求参数content生成对应的二维码
 @RequestMapping(value = "/image", method = RequestMethod.GET)
 public ResponseEntity<byte[]> image(@RequestParam String content) {
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");//设置显示图片
        headers.add("Cache-Control", "max-age=604800"); //设置缓存
        headers.add("Content-Disposition", "attachment;filename=image.JPG");
        byte[] bytes = QRCodeGenerator.builder().build().generateQrCode(content);
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    } catch (Exception ex) {
        return null;
    }
 }
 */
}
