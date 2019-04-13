package com.github.net.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alex.chen
 * @Description:
 * @date 2018/9/11.
 */
public class Httpclient4_test {

    @Test
    public void getRequest() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(new HttpGet("https://blog.csdn.net/alex_xfboy/article/details/77942273?hello=world"))) {
            HttpEntity entity = response.getEntity();
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
            System.out.println(EntityUtils.toString(response.getEntity()));
            // and ensure it is fully consumed
            EntityUtils.consume(entity);
        }
    }

    @Test
    public void postRequest() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://blog.csdn.net/alex_xfboy/article/details/77942273");
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("username", "biezhi"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line).append("\r\n");
            }
            System.out.println(result);
            // and ensure it is fully consumed
            EntityUtils.consume(entity);
        }
    }

    @Test
    public void setTimeout() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        HttpGet httpGet = new HttpGet("https://blog.csdn.net/alex_xfboy");
        httpGet.setConfig(requestConfig);

        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            System.out.println(response.getStatusLine());
        }
    }

    @Test
    public void addCookie() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("BIEZHI_SESSIONID", "1234");
        cookie.setDomain(".httpbin.org");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        try (CloseableHttpResponse response = httpclient.execute(new HttpGet("https://blog.csdn.net/alex_xfboy/article/details/77942273"), localContext)) {
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println(cookieStore.getCookies());
        }
    }

    @Test
    public void getMimeType() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(new HttpGet("https://blog.csdn.net/alex_xfboy/article/details/77942273"))) {
            HttpEntity entity = response.getEntity();
            String contentMimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
            System.out.println(contentMimeType);
            System.out.println(contentMimeType.equals(ContentType.APPLICATION_JSON.getMimeType()));
            // and ensure it is fully consumed
            EntityUtils.consume(entity);
        }
    }

    @Test
    public void noRedirect() throws IOException {
        CloseableHttpClient httpclient = HttpClientBuilder.create().disableRedirectHandling().build();
        try (CloseableHttpResponse response = httpclient.execute(new HttpGet("https://blog.csdn.net/alex_xfboy/article/details/77942273"))) {
            System.out.println(response.getStatusLine());
            System.out.println(response.getStatusLine().getStatusCode() == 302);
        }
    }
}
