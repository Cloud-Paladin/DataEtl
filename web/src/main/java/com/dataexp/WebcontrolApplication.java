package com.dataexp;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import com.dataexp.graph.logic.LogicGraph;
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
        new LogicGraph();
        System.out.println("服务端开启....");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
        LOG.error("error day comes");
    }

}

