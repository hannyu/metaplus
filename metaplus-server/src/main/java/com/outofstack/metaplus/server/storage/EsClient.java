package com.outofstack.metaplus.server.storage;

import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.http.JsonObjectMessageConverter;
import com.outofstack.metaplus.server.MetaplusConfig;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

/**
 * Based on Spring RestClient, which is thread safe.
 *
 * FIXME: 配置、安全
 */
@Component
public class EsClient {
    private static final Logger log = LoggerFactory.getLogger(EsClient.class);

    @Autowired
    private MetaplusConfig metaplusConfig;

    RestClient restClient;

    @PostConstruct
    public void init() {
        restClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .messageConverters(converters -> {
                    converters.add(0, new JsonObjectMessageConverter());
                })
                .baseUrl(metaplusConfig.getEs().getBaseUrl())
//                .baseUrl("http://localhost:9200/")
//                .baseUrl("http://localhost:5120/")
                .build();
    }


    public EsResponse get(String path) {
        return get(path, null);
    }

    public EsResponse get(String path, JsonObject requestBody) {
        log.debug("== GET {}, body: {}", path, requestBody);
        try {
            ResponseEntity<JsonObject> response;
            if (null == requestBody) {
                response = restClient.method(HttpMethod.GET)
                        .uri(path)
//                      .contentType(MediaType.APPLICATION_JSON)
//                      .body(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                        .toEntity(JsonObject.class);
            } else {
                response = restClient.method(HttpMethod.GET)
                        .uri(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                        .toEntity(JsonObject.class);
            }
            log.debug("   res: {}", response);
            return transfer(response);
        } catch (RuntimeException e) {
            throw new EsRuntimeException("EsClient.get '" + path + "' failed", e);
        }
    }

    public EsResponse post(String path, JsonObject requestBody) {
        log.debug("== POST {}, body: {}", path, requestBody);
        try {
            ResponseEntity<JsonObject> response = restClient.post()
                    .uri(path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                    .toEntity(JsonObject.class);
            log.debug("   res: {}", response);
            return transfer(response);
        } catch (RuntimeException e) {
            throw new EsRuntimeException("EsClient.post '" + path + "' failed", e);
        }
    }


    public EsResponse put(String path, JsonObject requestBody) {
        log.debug("== PUT {}, body: {}", path, requestBody);
        try {
            ResponseEntity<JsonObject> response = restClient.put()
                    .uri(path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                    .toEntity(JsonObject.class);
            log.debug("   res: {}", response);
            return transfer(response);
        } catch (RuntimeException e) {
            throw new EsRuntimeException("EsClient.put '" + path + "' failed", e);
        }
    }

    public EsResponse delete(String path) {
        log.debug("== DELETE {}", path);
        try {
            ResponseEntity<JsonObject> response = restClient.delete()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                    .toEntity(JsonObject.class);
            log.debug("   res: {}", response);
            return transfer(response);
        } catch (RuntimeException e) {
            throw new EsRuntimeException("EsClient.delete '" + path + "' failed", e);
        }
    }


    public EsResponse head(String path) {
        log.debug("== HEAD {}", path);
        try {
            ResponseEntity<JsonObject> response = restClient.head()
                    .uri(path)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                    .toEntity(JsonObject.class);
            log.debug("   res: {}", response);
            return transfer(response);
        } catch (RuntimeException e) {
            throw new EsRuntimeException("EsClient.head '" + path + "' failed", e);
        }
    }


    private EsResponse transfer(ResponseEntity<JsonObject> response) {
        return new EsResponse(response.getStatusCode().value(), response.getBody());
    }


}
