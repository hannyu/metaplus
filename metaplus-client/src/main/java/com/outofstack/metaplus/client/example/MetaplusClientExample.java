package com.outofstack.metaplus.client.example;

import com.outofstack.metaplus.client.MetaplusClient;
import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.http.JsonObjectMessageConverter;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.search.Hits;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class MetaplusClientExample {

    public static void main(String[] args) {

        RestClient restClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .messageConverters(converters -> {
                    converters.add(0, new JsonObjectMessageConverter());
                })
                .baseUrl("http://localhost:8020/")
                .build();
        MetaplusClient mpclient = new MetaplusClient(restClient);
        /// or simply
        /// MetaplusClient mpClient = new MetaplusClient("http://localhost:8020/");


        /// /hello
        HttpResponse<JsonObject> response1 = mpclient.hello();
        System.out.println("hello res: " + response1);

        /// /wrong
        response1 = mpclient.wrong();
        System.out.println("wrong res: " + response1);

        /// /echo
        response1 = mpclient.echo("Jerry", new JsonObject("Tom", "is here"));
        System.out.println("echo res: " + response1);

        /// /domain/list
        HttpResponse<JsonObject> response2 = mpclient.domainList();
        System.out.println("domainList res: " + response2);

        /// /query/read
        HttpResponse<MetaplusDoc> response3 = mpclient.queryRead("metaplus::domain::none");
        System.out.println("queryRead res: " + response3);

        /// /query/simple_search
        HttpResponse<Hits> response4 = mpclient.querySimpleSearch("*", "2025");
        System.out.println("querySimpleSearch res: " + response4);

    }
}
