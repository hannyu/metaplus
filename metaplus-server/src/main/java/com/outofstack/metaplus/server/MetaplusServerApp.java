package com.outofstack.metaplus.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MetaplusServerApp {

    public static void main(String[] args) {
//        SpringApplication.run(MetaplusServerApp.class);
        System.out.println("====================== main begin");
        new SpringApplicationBuilder(MetaplusServerApp.class)
                .properties("spring.config.location=classpath:/metaplus_server.yml").run(args);
        System.out.println("====================== main end");

    }

}
