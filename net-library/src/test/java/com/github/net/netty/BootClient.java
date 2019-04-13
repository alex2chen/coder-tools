package com.github.net.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * @author alex.chen
 * @Description:
 * @date 2017/4/13
 */
public class BootClient {
    @Test
    public void request() throws Exception {
        int port = 8080;
        connect(port, "127.0.0.1");
    }

    public void connect(int port, String host) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        // 作用是当创建NioSocketChannel成功之后，在初始化它的时候将它的ChannelHandler设置到ChannelPipeline中
                        // 用于处理网络IO事件
                        @Override
                        protected void initChannel(SocketChannel arg0) throws Exception {
                            arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            arg0.pipeline().addLast(new StringDecoder());
                            arg0.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();
            // 等待客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

    public static class TimeClientHandler extends ChannelInboundHandlerAdapter {
        private final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
        private int counter;
        private byte[] req;

        /**
         * Creates a client-side handler
         */
        public TimeClientHandler() {
            req = (("QUERY TIME ORDER") + System.getProperty("line.separator")).getBytes();
        }

        /**
         * 当客户端和服务器端TCP链路建立成功之后，Netty的NIO线程会调用channelActive方法<br>
         * 发送查询时间的指令给服务端，调用ChannelHandlerContext的writeAndFlush方法将请求消息发送给服务端<br>
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(firstMessage);
            ByteBuf message = null;
            for (int i = 0; i < 100; i++) {
                message = Unpooled.buffer(req.length);
                message.writeBytes(req);
                ctx.writeAndFlush(message);
            }
        }

        /**
         * 当服务端返回应答消息时，channelRead方法被调用
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf)msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req, "UTF-8");

            String body = (String) msg;
            System.out.println("Now is : " + body + " ; the counter is : " + ++counter);
        }

        /**
         * 当发生异常时，打印异常日志，释放客户端资源
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            // 释放资源
            logger.warning("Unexpected exception from downstream : " + cause.getMessage());
            ctx.close();
        }
    }

}
