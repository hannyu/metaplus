package com.outofstack.metaplus.client.example;

import com.outofstack.metaplus.client.MetaplusClient;
import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.http.JsonObjectMessageConverter;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.search.Hits;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class UseMetaplusClientExample {

    public static void main(String[] args) {

        RestClient restClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .messageConverters(converters -> {
                    converters.add(0, new JsonObjectMessageConverter());
                })
                .baseUrl("http://localhost:8020/")
                .build();
        MetaplusClient mpClient = new MetaplusClient(restClient);

        /// /hello
        HttpResponse<JsonObject> response1 = mpClient.hello();
        System.out.println("hello res: " + response1);

        /// /wrong
        response1 = mpClient.wrong();
        System.out.println("wrong res: " + response1);

        /// /echo
        response1 = mpClient.echo("Jerry", new JsonObject("Tom", "is here"));
        System.out.println("echo res: " + response1);

        /// /domain/list
        HttpResponse<JsonObject> response2 = mpClient.domainList();
        System.out.println("domainList res: " + response2);

        /// /doc/read
        HttpResponse<MetaplusDoc> response3 = mpClient.docRead("metaplus::domain::none");
        System.out.println("docRead res: " + response3);

        /// /search/simple
        HttpResponse<Hits> response4 = mpClient.searchSimple("*", "2025");
        System.out.println("searchSimple res: " + response4);

    }
}
