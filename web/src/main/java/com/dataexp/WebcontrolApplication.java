package com.dataexp;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import com.dataexp.graph.logic.LogicGraph;
import com.dataexp.thriftserver.Hello;
import com.dataexp.thriftserver.impl.HelloServiceImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class WebcontrolApplication {

    private static final Logger LOG = LoggerFactory.getLogger(WebcontrolApplication.class);

    public static void main(String[] args) {
//        SpringApplication.run(WebcontrolApplication.class, args);
        SpringApplication app = new SpringApplication(WebcontrolApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
        try {
            new LogicGraph();
            System.out.println("服务端开启....");
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            StatusPrinter.print(lc);
            LOG.error("error day comes");
            TProcessor tprocessor = new Hello.Processor<Hello.Iface>(new HelloServiceImpl());
            // 简单的单线程服务模型
            TServerSocket serverTransport = new TServerSocket(9898);
            TServer.Args tArgs = new TServer.Args(serverTransport);
            tArgs.processor(tprocessor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            TServer server = new TSimpleServer(tArgs);
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }


    }

}

