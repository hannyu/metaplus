package com.outofstack.metaplus.server;

import com.outofstack.metaplus.common.http.JsonObjectMessageConverter;
import com.outofstack.metaplus.common.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;


@Slf4j
@SpringBootApplication
public class MetaplusServerApp {

    public static void main(String[] args) {
//        System.out.println("====================== main begin");
        log.info("====== Start MetaplusServerApp ...");
        new SpringApplicationBuilder(MetaplusServerApp.class)
                .properties("spring.config.location=classpath:/metaplus_server.yml")
                .run(args);
//        System.out.println("====================== main end");

    }

    @Bean
    public HttpMessageConverter<JsonObject> createJsonObjectMessageConverter() {
        return new JsonObjectMessageConverter();
    }
}
