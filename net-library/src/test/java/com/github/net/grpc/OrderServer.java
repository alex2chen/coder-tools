package com.github.net.grpc;

import com.github.net.grpc.contract.OrderServiceProvider;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/5/10.
 */
public class OrderServer {
    private int port = 9527;

    @Test
    public void start() throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(port)
                .addService(new OrderServiceProvider()).build().start();
        System.out.println("server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                // Use JVM shutdown hook.
                System.err.println("shutting down gRPC server");
                server.shutdown();
            }
        });
        server.awaitTermination();
    }

}
