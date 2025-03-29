package com.outofstack.metaplus.client.example;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.http.JsonObjectMessageConverter;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.search.Hits;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class SpringRestClientExample {

    public static void main(String[] args) {

        RestClient restClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .messageConverters(converters -> {
                    converters.add(0, new JsonObjectMessageConverter());
                })
                .baseUrl("http://localhost:8020/")
                .build();

        /// /search/simple
        ResponseEntity<JsonObject> response = restClient.get()
                .uri("/search/simple/%s/%s".formatted("*", "I love you"))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        System.out.println("response: " + response);

        HttpResponse<Hits> hr = new HttpResponse<Hits>(response.getBody(), Hits.class);
        System.out.printf("code: %s, msg: %s, hits: %s%n", hr.getCode(), hr.getMsg(), hr.getBody());
    }
}
