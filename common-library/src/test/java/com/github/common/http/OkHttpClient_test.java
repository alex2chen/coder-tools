package com.github.common.http;

import okhttp3.*;
import org.apache.http.HttpResponse;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/10/11.
 */
public class OkHttpClient_test {
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private String url = "http://wwww.baidu.com";
    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Test
    public void get() throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println(response.body().string());
        } else {
            System.out.println("Unexpected code " + response);
        }
    }

    @Test
    public void post() throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();
        Request request = new Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(requestBody)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        System.out.println(response);
    }

    @Test
    public void callBack() throws InterruptedException {
        Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("failed: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("completed: " + response.body().string());
            }
        });
        Thread.sleep(3000);
    }
}
