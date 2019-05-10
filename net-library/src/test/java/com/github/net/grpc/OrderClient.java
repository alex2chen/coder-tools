package com.github.net.grpc;

import com.github.net.grpc.contract.OrderServiceGrpc;
import com.github.net.grpc.dto.OrderDTO;
import com.github.net.grpc.dto.OrderId;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/5/10.
 */
public class OrderClient {
    private String host = "localhost";
    private int serverPort = 9527;
    private ManagedChannel managedChannel;
    private OrderServiceGrpc.OrderServiceBlockingStub orderConsumer;
    private OrderServiceGrpc.OrderServiceFutureStub orderAsyncConsumer;
    private OrderServiceGrpc.OrderServiceStub orderStreamConsumer;
    private OrderId orderDTO = OrderId.newBuilder().setId(123).build();

    @Before
    public void init() {
        managedChannel = ManagedChannelBuilder.forAddress(host, serverPort)
                //Channels are secure by default (via SSL/TLS)
                .usePlaintext().
                        build();
        orderConsumer = OrderServiceGrpc.newBlockingStub(managedChannel);
        orderAsyncConsumer = OrderServiceGrpc.newFutureStub(managedChannel);
        orderStreamConsumer = OrderServiceGrpc.newStub(managedChannel);
    }

    @After
    public void closed() throws InterruptedException {
        managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    public void getOrder() {
        OrderDTO result = orderConsumer.getOrder(orderDTO);
        System.out.println(result);
    }

    @Test
    public void addOrder() {
        OrderDTO orderDTO = OrderDTO.newBuilder().setName("iphone").build();
        OrderId result = orderConsumer.addOrder(orderDTO);
        System.out.println(result);
    }

    @Test
    public void queryOrder() {
        OrderDTO orderDTO = OrderDTO.newBuilder().setName("iphone").build();
        Iterator<OrderId> iterator = orderConsumer.queryOrder(orderDTO);
        while (iterator.hasNext()) {
            System.out.println("queryOrder:" + iterator.next());
        }
    }

    @Test
    public void asyncGetOrder() {
        ListenableFuture<OrderDTO> result = orderAsyncConsumer.getOrder(orderDTO);
        System.out.println(result);
    }

    @Test
    public void streamGetOrder() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<OrderDTO> responseObserver = new StreamObserver<OrderDTO>() {
            @Override
            public void onNext(OrderDTO value) {
                System.out.println("get result :" + value);
            }

            @Override
            public void onError(Throwable t) {
                Status status = Status.fromThrowable(t);
                System.out.println("failed with status : " + status);
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("finished!");
                latch.countDown();
            }
        };
        orderStreamConsumer.getOrder(orderDTO, responseObserver);
        latch.await();
    }
}
