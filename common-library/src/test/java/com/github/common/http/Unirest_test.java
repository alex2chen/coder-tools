package com.github.common.http;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

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
    public void request() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://httpbin.org/post")
                .header("accept", "application/json")
                .field("parameter", "value")
                .field("foo", "bar")
                .asJson();

        //参数处理
        Unirest.get("http://httpbin.org/{method}")
                .routeParam("method", "get")
                .header("name", "Mark")
                .asJson();
        //异步请求
        Future<HttpResponse<JsonNode>> future = Unirest.post("http://httpbin.org/post")
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
}
