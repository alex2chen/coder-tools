package com.github.net.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description: httpclient在4.x之后开始提供基于nio的异步版本httpasyncclient
 * @Date: created in 2018/4/15.
 */
public class HttpAsyncClient_test {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAsyncClient_test.class);
    private CloseableHttpAsyncClient httpClient;

    @Before
    public void init() {
        httpClient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(ConnectionPoolConfigFactory.defaultConfig)
                .setConnectionManager(ConnectionPoolConfigFactory.connectionManager)
                .build();
        httpClient.start();
    }

    @Test
    public void asyncGet() throws InterruptedException {
        httpClient.execute(new HttpGet("https://blog.csdn.net/alex_xfboy/article/details/77942273"), new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                System.out.println("suc:" + httpResponse.getStatusLine());
            }

            @Override
            public void failed(Exception e) {
                System.out.println("failed:" + e);
            }

            @Override
            public void cancelled() {
                System.out.println("cancelled");
            }
        });
        Thread.sleep(2000);
    }

    public static class ConnectionPoolConfigFactory {
        private static int ioThreadCount = 80;
        private static int maxConnection = 200; //连接池最大数量
        private static int defaultMaxRoute = 50; //单个路由最大连接数量
        private static int connectTimeOut = 60000 * 5;//链接目标url最大耗时，超过设定值还没有连上，报timeout
        private static int connectionRequestTimeOut = 60000 * 5;
        private static int socketTimeOut = 600000;//等待回放response的最大等待时间，超过就报timeout
        private static PoolingNHttpClientConnectionManager connectionManager;
        private static RequestConfig defaultConfig;

        static {
            defaultConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(connectionRequestTimeOut)
                    .setConnectTimeout(connectTimeOut)
                    .setSocketTimeout(socketTimeOut)
                    .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .build();
            try {
                //配置io线程
                IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(ioThreadCount).build();
                ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
                //设置连接池大小
                connectionManager = new PoolingNHttpClientConnectionManager(ioReactor);
                connectionManager.setMaxTotal(maxConnection);
                connectionManager.setDefaultMaxPerRoute(defaultMaxRoute);
            } catch (Exception e) {
                LOGGER.error("创建HTTP请求池出错：" + e.getMessage(), e);
            }
        }
    }

}
