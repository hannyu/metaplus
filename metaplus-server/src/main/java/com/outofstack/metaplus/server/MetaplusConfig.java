package com.outofstack.metaplus.server;


import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("metaplus")
@Data
public class MetaplusConfig {

    private String profile;
    private Es es;
    private Rest rest;

    @Data
    public static class Es {
        private String baseUrl;
    }

    @Data
    public static class Rest {
        private String host;
        private String port;
    }

}
