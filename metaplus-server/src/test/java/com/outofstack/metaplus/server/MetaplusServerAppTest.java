package com.outofstack.metaplus.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class MetaplusServerAppTest {

    public static void main(String[] args) {
        System.out.println("====================== main begin");
        new SpringApplicationBuilder(MetaplusServerAppTest.class)
                .properties("spring.config.location=classpath:/metaplus_server.yml").run(args);
        System.out.println("====================== main end");
    }

    @Autowired
    private MetaplusConfig metaplusConfig;
}
