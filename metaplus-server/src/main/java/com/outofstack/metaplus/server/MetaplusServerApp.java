package com.outofstack.metaplus.server;

import com.outofstack.metaplus.common.http.JsonObjectMessageConverter;
import com.outofstack.metaplus.common.json.JsonObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;


@SpringBootApplication
public class MetaplusServerApp {

    public static void main(String[] args) {
//        System.out.println("====================== main begin");
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
