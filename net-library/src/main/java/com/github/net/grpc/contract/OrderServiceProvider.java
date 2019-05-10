package com.github.net.grpc.contract;

import com.github.net.grpc.dto.OrderDTO;
import com.github.net.grpc.dto.OrderId;
import io.grpc.stub.StreamObserver;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/5/10.
 */
public class OrderServiceProvider extends OrderServiceGrpc.OrderServiceImplBase{
    @Override
    public void getOrder(OrderId request, StreamObserver<OrderDTO> responseObserver) {
        responseObserver.onNext(OrderDTO.newBuilder().setName("test").build());
        responseObserver.onCompleted();
    }

    @Override
    public void addOrder(OrderDTO request, StreamObserver<OrderId> responseObserver) {
        responseObserver.onNext(OrderId.newBuilder().setId(9527).build());
        responseObserver.onCompleted();
    }

    @Override
    public void queryOrder(OrderDTO request, StreamObserver<OrderId> responseObserver) {
        responseObserver.onNext(OrderId.newBuilder().setId(9527).build());
        responseObserver.onNext(OrderId.newBuilder().setId(9528).build());
        responseObserver.onCompleted();
    }
}
