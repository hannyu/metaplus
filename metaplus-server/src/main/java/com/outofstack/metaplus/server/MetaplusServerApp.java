package com.outofstack.metaplus.server;

import com.outofstack.metaplus.common.http.JsonObjectMessageConverter;
import com.outofstack.metaplus.common.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.EncodedSolidusHandling;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;


@Slf4j
@SpringBootApplication
public class MetaplusServerApp {

    public static void main(String[] args) {
        log.info("====== Start MetaplusServerApp ...");

        /// fix: if '%2F' in URL, spring will return 400 HTML.
        System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");

        new SpringApplicationBuilder(MetaplusServerApp.class)
                .properties("spring.config.location=classpath:/metaplus_server.yml")
                .run(args);
    }

    @Bean
    public HttpMessageConverter<JsonObject> createJsonObjectMessageConverter() {
        return new JsonObjectMessageConverter();
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        log.info("Configuring Tomcat to allow encoded slashes.");
        return factory -> factory.addConnectorCustomizers(
                connector -> connector.setEncodedSolidusHandling(EncodedSolidusHandling.DECODE.getValue()));
    }

}
