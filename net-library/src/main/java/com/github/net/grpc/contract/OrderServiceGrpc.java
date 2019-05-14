package com.github.net.grpc.contract;

import com.github.net.grpc.dto.OrderApi;
import com.github.net.grpc.dto.OrderDTO;
import com.github.net.grpc.dto.OrderId;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.2.0)",
    comments = "Source: order.proto")
public final class OrderServiceGrpc {

  private OrderServiceGrpc() {}

  public static final String SERVICE_NAME = "OrderService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<OrderId,
          OrderDTO> METHOD_GET_ORDER =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "OrderService", "getOrder"),
          io.grpc.protobuf.ProtoUtils.marshaller(OrderId.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(OrderDTO.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<OrderDTO,
          OrderId> METHOD_ADD_ORDER =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "OrderService", "addOrder"),
          io.grpc.protobuf.ProtoUtils.marshaller(OrderDTO.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(OrderId.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<OrderDTO,
          OrderId> METHOD_QUERY_ORDER =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
          generateFullMethodName(
              "OrderService", "queryOrder"),
          io.grpc.protobuf.ProtoUtils.marshaller(OrderDTO.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(OrderId.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static OrderServiceStub newStub(io.grpc.Channel channel) {
    return new OrderServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static OrderServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new OrderServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static OrderServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new OrderServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class OrderServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getOrder(OrderId request,
                         io.grpc.stub.StreamObserver<OrderDTO> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_ORDER, responseObserver);
    }

    /**
     */
    public void addOrder(OrderDTO request,
                         io.grpc.stub.StreamObserver<OrderId> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_ORDER, responseObserver);
    }

    /**
     */
    public void queryOrder(OrderDTO request,
                           io.grpc.stub.StreamObserver<OrderId> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_QUERY_ORDER, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_GET_ORDER,
            asyncUnaryCall(
              new MethodHandlers<
                      OrderId,
                      OrderDTO>(
                  this, METHODID_GET_ORDER)))
          .addMethod(
            METHOD_ADD_ORDER,
            asyncUnaryCall(
              new MethodHandlers<
                      OrderDTO,
                      OrderId>(
                  this, METHODID_ADD_ORDER)))
          .addMethod(
            METHOD_QUERY_ORDER,
            asyncServerStreamingCall(
              new MethodHandlers<
                      OrderDTO,
                      OrderId>(
                  this, METHODID_QUERY_ORDER)))
          .build();
    }
  }

  /**
   */
  public static final class OrderServiceStub extends io.grpc.stub.AbstractStub<OrderServiceStub> {
    private OrderServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private OrderServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new OrderServiceStub(channel, callOptions);
    }

    /**
     */
    public void getOrder(OrderId request,
                         io.grpc.stub.StreamObserver<OrderDTO> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_ORDER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addOrder(OrderDTO request,
                         io.grpc.stub.StreamObserver<OrderId> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_ORDER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryOrder(OrderDTO request,
                           io.grpc.stub.StreamObserver<OrderId> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_QUERY_ORDER, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class OrderServiceBlockingStub extends io.grpc.stub.AbstractStub<OrderServiceBlockingStub> {
    private OrderServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private OrderServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new OrderServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public OrderDTO getOrder(OrderId request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_ORDER, getCallOptions(), request);
    }

    /**
     */
    public OrderId addOrder(OrderDTO request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_ORDER, getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<OrderId> queryOrder(
        OrderDTO request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_QUERY_ORDER, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class OrderServiceFutureStub extends io.grpc.stub.AbstractStub<OrderServiceFutureStub> {
    private OrderServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private OrderServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new OrderServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<OrderDTO> getOrder(
        OrderId request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_ORDER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<OrderId> addOrder(
        OrderDTO request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_ORDER, getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_ORDER = 0;
  private static final int METHODID_ADD_ORDER = 1;
  private static final int METHODID_QUERY_ORDER = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final OrderServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(OrderServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ORDER:
          serviceImpl.getOrder((OrderId) request,
              (io.grpc.stub.StreamObserver<OrderDTO>) responseObserver);
          break;
        case METHODID_ADD_ORDER:
          serviceImpl.addOrder((OrderDTO) request,
              (io.grpc.stub.StreamObserver<OrderId>) responseObserver);
          break;
        case METHODID_QUERY_ORDER:
          serviceImpl.queryOrder((OrderDTO) request,
              (io.grpc.stub.StreamObserver<OrderId>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class OrderServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return OrderApi.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (OrderServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new OrderServiceDescriptorSupplier())
              .addMethod(METHOD_GET_ORDER)
              .addMethod(METHOD_ADD_ORDER)
              .addMethod(METHOD_QUERY_ORDER)
              .build();
        }
      }
    }
    return result;
  }
}
