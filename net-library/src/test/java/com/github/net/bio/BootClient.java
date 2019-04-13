package com.github.net.bio;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @Author: alex
 * @Description: BIO的TimeClient
 * @Date: created in 2015/7/18.
 */
public class BootClient {
    @Test
    public void request() {
        int port = 8080;
        int i = 0;
        while (i++ < 1000) {
            new Thread(new TimeClientHandler(port), "BootClient-" + i).start();
        }
    }

    public static class TimeClientHandler implements Runnable {
        private int port;

        public TimeClientHandler(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            Socket socket = null;
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                socket = new Socket("127.0.0.1", port);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                // Sleep 0-10 s中的随机时间
                Thread.sleep((long) (1000 * Math.random() * 10));
                out.println("QUERY TIME ORDER");//会被阻塞
                System.out.println("Send order to server succeed.");

                String resp = in.readLine();
                System.out.println("Now is : " + resp);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(out);
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(socket);
            }
        }

    }
}
