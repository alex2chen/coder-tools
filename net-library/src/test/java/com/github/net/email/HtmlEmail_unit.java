package com.github.net.email;

import org.apache.commons.mail.*;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author alex.chen
 * @Description:
 * @date 2018/10/11.
 */
public class HtmlEmail_unit {
    @Test
    public void sendText() throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("username", "password"));
        email.setSSLOnConnect(true);
        email.setFrom("user@gmail.com");
        email.setSubject("TestMail");
        email.setMsg("This is a test mail ... :-)");
        email.addTo("foo@bar.com");
        email.send();
    }

    @Test
    public void sendHtml() throws EmailException, MalformedURLException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName("mail.myserver.com");
        email.addTo("jdoe@somewhere.org", "John Doe");
        email.setFrom("me@apache.org", "Me");
        email.setSubject("Test email with inline image");
        URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
        String cid = email.embed(url, "Apache logo");
        email.setHtmlMsg("<html>The apache logo - <img src=\"cid:" + cid + "\"></html>");
        email.setTextMsg("Your email client does not support HTML messages");
        email.send();
    }

    @Test
    public void sendHtmlEmbeddedImage() throws EmailException, MalformedURLException {
        //发送HTML格式的邮件与嵌入式图像
        String htmlEmailTemplate = ".... <img src=\"http://www.apache.org/images/feather.gif\"> ....";
        URL url = new URL("http://www.apache.org");
        ImageHtmlEmail email = new ImageHtmlEmail();
        email.setDataSourceResolver(new DataSourceUrlResolver(url));
        email.setHostName("mail.myserver.com");
        email.addTo("jdoe@somewhere.org", "John Doe");
        email.setFrom("me@apache.org", "Me");
        email.setSubject("Test email with inline image");
        email.setHtmlMsg(htmlEmailTemplate);
        email.setTextMsg("Your email client does not support HTML messages");
        email.send();
    }

    @Test
    public void sendWithAttach() throws EmailException {
        //发送带有附件的电子邮件
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath("mypictures/john.jpg");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("Picture of John");
        attachment.setName("John");
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("mail.myserver.com");
        email.addTo("jdoe@somewhere.org", "John Doe");
        email.setFrom("me@apache.org", "Me");
        email.setSubject("The picture");
        email.setMsg("Here is the picture you wanted");
        email.attach(attachment);
        email.send();
    }
}
