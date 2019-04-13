package com.github.net.bio;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description: BIO的TimeServer
 * @Date: created in 2015/7/18.
 */
public class BootServer {
    private int port = 8080;

    @Test
    public void startMulitThread() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(server);
            System.out.println("The time server close");
        }
    }

    /**
     * 伪异步IO的TimeServer
     * @throws IOException
     */
    @Test
    public void startExecutePool() throws IOException {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 10000);
            while (true) {
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(server);
            System.out.println("The time server close");
        }
    }

    public static class TimeServerHandler implements Runnable {
        private Socket socket;

        public TimeServerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                out = new PrintWriter(this.socket.getOutputStream(), true);

                String currentTime = null;
                String body = null;
                while (true) {
                    // 此处如果无数据，则会被阻塞,直到: 1. 有数据可读; 2. 可用数据读取完毕(客户端输出流关闭); 3.发生空指针或IO异常
                    body = in.readLine();
                    if (body == null) {
                        break;
                    }
                    System.out.println("The time server receive order : " + body);
                    currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                    out.println(currentTime);
                }
            } catch (Exception e) {
                IOUtils.closeQuietly(out);
                IOUtils.closeQuietly(socket);
            }
        }

    }

    public static class TimeServerHandlerExecutePool {
        private ExecutorService executor;

        public TimeServerHandlerExecutePool(int maxPoolSize, int queueSize) {
            executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                    maxPoolSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
        }

        public void execute(Runnable task) {
            executor.execute(task);
        }
    }
}
