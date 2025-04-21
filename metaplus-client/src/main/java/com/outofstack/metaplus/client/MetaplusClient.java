package com.outofstack.metaplus.client;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.http.JsonObjectMessageConverter;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.DomainDoc;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.MetaplusPatch;
import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.common.model.search.Query;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MetaplusClient {

    private final RestClient restClient;

    public MetaplusClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public MetaplusClient(String baseUrl) {
        this.restClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .messageConverters(converters -> {
                    converters.add(0, new JsonObjectMessageConverter());
                })
                .baseUrl(baseUrl)
                .build();
    }

    public RestClient getRestClient() {
        return restClient;
    }

    /// ///////////////////////
    /// hello, wrong, echo
    /// ///////////////////////

    public HttpResponse<JsonObject> hello() {
        ResponseEntity<JsonObject> response = restClient.get()
                .uri("/hello")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<JsonObject> wrong() {
        ResponseEntity<JsonObject> response = restClient.get()
                .uri("/wrong")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<JsonObject> echo(String name, JsonObject requestBody) {
        ResponseEntity<JsonObject> response = restClient.post()
                .uri("/echo/{name}", name)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    /// ///////////////////////
    /// meta, plus, patch
    /// ///////////////////////

    public HttpResponse<JsonObject> metaCreate(String fqmn, MetaplusDoc doc) {
        ResponseEntity<JsonObject> response = restClient.put()
                .uri("/meta/create/{fqmn}", fqmn)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(doc)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<JsonObject> metaUpdate(String fqmn, MetaplusDoc doc) {
        ResponseEntity<JsonObject> response = restClient.post()
                .uri("/meta/update/{fqmn}", fqmn)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(doc)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<JsonObject> metaDelete(String fqmn, MetaplusDoc doc) {
        ResponseEntity<JsonObject> response = restClient.method(HttpMethod.DELETE)
                .uri("/meta/delete/{fqmn}", fqmn)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(doc)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<JsonObject> plusUpdate(String fqmn, MetaplusDoc doc) {
        ResponseEntity<JsonObject> response = restClient.post()
                .uri("/plus/update/{fqmn}", fqmn)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(doc)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<JsonObject> patchUpdate(String domain, MetaplusPatch patch) {
        ResponseEntity<JsonObject> response = restClient.post()
                .uri("/patch/update/{domain}", domain)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(patch)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<JsonObject> patchDelete(String domain, MetaplusPatch patch) {
        ResponseEntity<JsonObject> response = restClient.post()
                .uri("/patch/delete/{domain}", domain)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(patch)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }


    /// ///////////////////////
    /// sync
    /// ///////////////////////

    public HttpResponse<JsonObject> syncOne(MetaplusPatch patch) {
        ResponseEntity<JsonObject> response = restClient.post()
                .uri("/sync/one/{domain}", patch.getDomain())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(patch)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    /// ///////////////////////
    /// query
    /// ///////////////////////

    public HttpResponse<JsonObject> queryExist(String fqmn) {
        ResponseEntity<JsonObject> response = restClient.head()
                .uri("/query/exist/{fqmn}", fqmn)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<MetaplusDoc> queryRead(String fqmn) {
        ResponseEntity<JsonObject> response = restClient.get()
                .uri("/query/read/{fqmn}", fqmn)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<MetaplusDoc>(response.getBody(), MetaplusDoc.class);
    }

    public HttpResponse<Hits> querySimpleSearch(String domains, String queryText) {
        ResponseEntity<JsonObject> response = restClient.get()
                .uri("/query/simple_search/{domains}/{queryText}", domains, queryText)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<Hits>(response.getBody(), Hits.class);
    }

    public HttpResponse<Hits> querySearch(String domains, Query query) {
        ResponseEntity<JsonObject> response = restClient.method(HttpMethod.GET)
                .uri("/query/search/{domains}", domains)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(query)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<Hits>(response.getBody(), Hits.class);
    }

    /// ///////////////////////
    /// domain
    /// ///////////////////////

    public HttpResponse<JsonObject> domainCreate(String domain, DomainDoc domainDoc) {
        ResponseEntity<JsonObject> response = restClient.put()
                .uri("/domain/create/{domain}", domain)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(domainDoc)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<JsonObject> domainUpdate(String domain, DomainDoc domainDoc) {
        ResponseEntity<JsonObject> response = restClient.post()
                .uri("/domain/update/{domain}", domain)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(domainDoc)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<JsonObject> domainDelete(String domain, DomainDoc domainDoc) {
        ResponseEntity<JsonObject> response = restClient.method(HttpMethod.DELETE)
                .uri("/domain/delete/{domain}", domain)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(domainDoc)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<MetaplusDoc> domainSample(String domain) {
        ResponseEntity<JsonObject> response = restClient.get()
                .uri("/domain/sample/{domain}", domain)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<MetaplusDoc>(response.getBody(), MetaplusDoc.class);
    }

    public HttpResponse<JsonObject> domainList() {
        ResponseEntity<JsonObject> response = restClient.get()
                .uri("/domain/list")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

}
