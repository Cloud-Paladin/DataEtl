package com.dataexp.dispatch;

import com.example.grpc.gencode.HelloRequest;
import com.example.grpc.gencode.HelloResponse;
import com.example.grpc.gencode.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class GrpcAgent {

    public static class ClientTask implements Runnable{
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            ManagedChannel channel = null;
            try {
                channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext(true).build();

                HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);

                HelloResponse helloResponse = stub
                        .hello(HelloRequest.newBuilder().setFirstName("Baeldung").setLastName("gRPC").build());

                System.out.println(helloResponse.getGreeting());
                long end = System.currentTimeMillis();
                System.out.println("this invoaction last "+ (end-start) +" milliseconds");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (channel != null) {
                    try {
                        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            Thread t = new Thread(new ClientTask());
            t.start();
            Thread.sleep(500+(int)Math.random()*1000);
        }
    }
}
