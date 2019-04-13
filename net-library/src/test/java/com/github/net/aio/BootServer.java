package com.github.net.aio;

import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: alex
 * @Description
 * @Date: created in 2015/7/18.
 */
public class BootServer {

    @Test
    public void start() {
        int port = 8080;
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }

    public static class AsyncTimeServerHandler implements Runnable {
        private int port;
        CountDownLatch latch;
        AsynchronousServerSocketChannel asynchronousServerSocketChannel;

        public AsyncTimeServerHandler(int port) {
            this.port = port;
            try {
                // 创建异步服务器通道，绑定监听端口
                asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
                asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
                System.out.println("The time server is start in port : " + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            // 初始化CountDownLatch对象，作用是在完成一组正在执行的操作之前，允许当前的线程一直阻塞
            latch = new CountDownLatch(1);
            doAccept();

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void doAccept() {
            // 接受客户端的连接，由于是异步操作，可以传递一个CompletionHandler<AsynchronousSocketChannel, ? super A>类型的实例
            // 来接受accept操作成功的通知消息
            asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
        }
    }

    public static class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {

        @Override
        public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
            /**
             * 再次调用AsyncTimeServerHandler.asynchronousServerSocketChannel的accept方法
             * 当我们调用AsynchronousServerSocketChannel的accept方法后，如果有新的客户端连接接入，系统将回调我们传入的CompletionHandler
             * 实例的completed方法，表示新的客户端已经接入成功，因为一个AsynchronousServerSocketChannel可以接受成千上万个客户端，
             * 所以我们需要继续调用它的accept方法，接受其他的客户端连接，最终形成一个循环。每当接受一个客户读取成功之后，
             * 再异步接受新的客户端连接
             */
            attachment.asynchronousServerSocketChannel.accept(attachment, this);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            /**
             * 异步读取操作
             * @param buffer 接受缓冲区，用于从异步Channel中读取数据包
             * @para buffer 异步Channel携带的附件，通知回调的时候作为入参使用
             * @para ReadCompletionHandler 接受通知回调的业务handler
             */
            result.read(buffer, buffer, new ReadCompletionHandler(result));
        }

        @Override
        public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
            exc.printStackTrace();
            attachment.latch.countDown();
        }
    }

    public static class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        private AsynchronousSocketChannel channel;

        public ReadCompletionHandler(AsynchronousSocketChannel channel) {
            if (this.channel == null) {
                this.channel = channel;
            }
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.flip();
            byte[] body = new byte[attachment.remaining()];
            attachment.get(body);

            try {
                String req = new String(body, "UTF-8");
                System.out.println("The time server receive order ; " + req);
                String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                doWrite(currentTime);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        private void doWrite(String currentTime) {
            if (currentTime != null && currentTime.trim().length() > 0) {
                byte[] bytes = currentTime.getBytes();
                ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                writeBuffer.put(bytes);
                writeBuffer.flip();

                channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

                    @Override
                    public void completed(Integer result, ByteBuffer buffer) {
                        // 如果没有发送完成，继续发送
                        if (buffer.hasRemaining()) {
                            channel.write(buffer, buffer, this);
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        try {
                            channel.close();
                        } catch (IOException e) {
                            // ingnore
                        }
                    }

                });
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            try {
                this.channel.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }

}
