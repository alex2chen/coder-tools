package com.github.net.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

/**
 * @author alex.chen
 * @Description:
 * @date 2017/4/13
 */
public class BootServer {
    @Test
    public void start() throws Exception {
        int port = 8080;
        bind(port);
    }
    public void bind(int port) throws Exception{
        // 配置服务端的NIO线程组
        // 用于服务端接受客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 用于进行SocketChannel的网络读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            // Netty用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

            // 绑定端口，同步等待成功
            // 类似java.util.concurrrent.Future,主要用于异步操作的通知回调
            ChannelFuture f = b.bind(port).sync();
            System.out.println("The time server is start in port : " + port);

            // 等待服务端监听端口关闭
            // 进行阻塞，等待服务端链路关闭后main函数才退出
            f.channel().closeFuture().sync();

        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 类似于Reactor模式中的handler，主要用于处理网络IO事件，例如日志记录，对消息进行编解码等
     *
     */
    public static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel arg0) throws Exception {
            // LineBasedFrameDecoder的工作原理是它依次遍历ByteBuf中的可读字节，判断看是否有"\n"或者"\r\n",
            // 如果有，就以此位置为结束位置，从可读索引到结束位置区间的字节就组成了一行
            // 如果连续读取到最大长度后仍然没有发现换行符，就不抛出异常，同时忽略掉之前读到的异常码流
            arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
            // StringDecoder的功能就是将接受到的对象转换成字符串，然后继续调用后面的handler
            // LineBaseFrameDecoder+StringDecoder组合就是按行切换的文本编码器，用来支持TCP的粘包和拆包
            arg0.pipeline().addLast(new StringDecoder());
            arg0.pipeline().addLast(new TimeServerHandler());

            // 除此之外，还有两种解码器，他们都能解决TCP粘包拆包导致的读半包问题
            // 1. DelimiterBasedFrameDecoder 自动完成以分隔符做结束标志的消息的解码
            // 2. FixedLengthFrameDecoder 自动完成对定长消息的解码
        }
    }
    public static class TimeServerHandler extends ChannelInboundHandlerAdapter {

        private int counter;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf)msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//
//        String body = new String(req, "UTF-8").substring(0, req.length - System.getProperty("line.separator").length());

            String body = (String) msg;

            System.out.println("The time server receive order : " + body + " ; the counter is : " + ++counter);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
            currentTime = currentTime + System.getProperty("line.separator");
            ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
            ctx.writeAndFlush(resp);
        }
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
}
