package com.github.net.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.*;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;

/**
 * Unirest 是一个轻量级的 HTTP 请求库，涵盖 Node、Ruby、Java、PHP、Python、Objective-C、.NET 等多种语言。
 *
 * @Author: alex
 * @Description:
 * @Date: created in 2018/3/11.
 */
public class Unirest_test {
    @Test
    public void get() throws UnirestException {
        //参数处理
        Unirest.get("https://blackhole.m.jd.com/{method}")
                .routeParam("method", "getinfo")
                .header("name", "Mark")
                .asJson();
    }

    @Test
    public void authentication() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.get("https://blackhole.m.jd.com/getinfo")
                .basicAuth("biezhi", "123456").asJson();
        System.out.println(jsonResponse.getBody().toString());
    }

    @Test
    public void post() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post("https://blackhole.m.jd.com/getinfo")
                .header("accept", "application/json")
                .field("parameter", "value")
                .field("foo", "bar")
                .asJson();
        System.out.println(jsonResponse);
    }

    @Test
    public void fileUpload() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post("https://blackhole.m.jd.com/post")
                .header("accept", "application/json")
                .field("parameter", "value")
                .field("file", new File("/tmp/file"))
                .asJson();
        System.out.println(jsonResponse.getBody().toString());
    }

    @Test
    public void asyncRequest() throws UnirestException {
        //异步请求
        Future<HttpResponse<JsonNode>> future = Unirest.post("https://blackhole.m.jd.com/post")
                .header("accept", "application/json")
                .field("param1", "value1")
                .field("param2", "value2")
                .asJsonAsync(new Callback<JsonNode>() {

                    public void failed(UnirestException e) {
                        System.out.println("The request has failed");
                    }

                    public void completed(HttpResponse<JsonNode> response) {
                        int code = response.getStatus();
                        Headers headers = response.getHeaders();
                        JsonNode body = response.getBody();
                        InputStream rawBody = response.getRawBody();
                    }

                    public void cancelled() {
                        System.out.println("The request has been cancelled");
                    }

                });
    }

    @Test
    public void serialization() throws UnirestException {
        // Only one time
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // Response to Object
        HttpResponse<Book> bookResponse = Unirest.get("https://blackhole.m.jd.com").asObject(Book.class);
        Book bookObject = bookResponse.getBody();
    }

    public static class Book {
    }
}
