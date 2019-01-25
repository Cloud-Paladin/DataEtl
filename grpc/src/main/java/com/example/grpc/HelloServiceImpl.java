package com.example.grpc;

import com.example.grpc.gencode.HelloRequest;
import com.example.grpc.gencode.HelloResponse;
import com.example.grpc.gencode.HelloServiceGrpc.HelloServiceImplBase;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Bing.Li
 * @create: 2019-01-25
 */
public class HelloServiceImpl extends HelloServiceImplBase {

    private static AtomicInteger count = new AtomicInteger(0);
    @Override
    public void hello(
            HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String greeting = new StringBuilder()
                .append("Hello, ")
                .append(request.getFirstName())
                .append(" ")
                .append(request.getLastName())
                .toString();
        System.out.println(Thread.currentThread().getName()+" : "+count.incrementAndGet());
        try {
            Thread.sleep((int)Math.random()*10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();
//        System.out.println("Get request: " + request.getFirstName() + " " + request.getLastName());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
