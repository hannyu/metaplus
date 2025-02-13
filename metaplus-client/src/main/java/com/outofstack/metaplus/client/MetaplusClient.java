package com.outofstack.metaplus.client;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.DomainDoc;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.MetaplusPatch;
import com.outofstack.metaplus.common.model.search.Hits;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class MetaplusClient {

    private final RestClient restClient;

    public MetaplusClient(RestClient restClient) {
        this.restClient = restClient;
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
                .uri("/echo/%s".formatted(name))
                .accept(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    /// ///////////////////////
    /// meta, plus, sync
    /// ///////////////////////

    public HttpResponse<JsonObject> metaCreate(String fqmn, MetaplusDoc doc) {
        ResponseEntity<JsonObject> response = restClient.put()
                .uri("/meta/create/%s".formatted(fqmn))
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
                .uri("/meta/update/%s".formatted(fqmn))
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
                .uri("/plus/update/%s".formatted(fqmn))
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
                .uri("/meta/delete/%s".formatted(fqmn))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(doc)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<JsonObject> metaExist(String fqmn) {
        ResponseEntity<JsonObject> response = restClient.head()
                .uri("/meta/exist/%s".formatted(fqmn))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    public HttpResponse<JsonObject> syncOne(MetaplusPatch patch) {
        ResponseEntity<JsonObject> response = restClient.post()
                .uri("/sync/one/%s".formatted(patch.getFqmnDomain()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(patch)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        return new HttpResponse<JsonObject>(response.getBody(), JsonObject.class);
    }

    /// ///////////////////////
    /// doc, search
    /// ///////////////////////

    public HttpResponse<MetaplusDoc> docRead(String fqmn) {
        ResponseEntity<JsonObject> response = restClient.get()
                .uri("/doc/read/%s".formatted(fqmn))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {/**/})
                .toEntity(JsonObject.class);
        System.out.println("body: " + response.getBody());
        return new HttpResponse<MetaplusDoc>(response.getBody(), MetaplusDoc.class);
    }

    public HttpResponse<Hits> searchSimple(String domains, String queryText) {
        ResponseEntity<JsonObject> response = restClient.get()
                .uri("/search/simple/%s/%s".formatted(domains, queryText))
                .accept(MediaType.APPLICATION_JSON)
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
                .uri("/domain/create/%s".formatted(domain))
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
                .uri("/domain/update/%s".formatted(domain))
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
                .uri("/domain/delete/%s".formatted(domain))
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
                .uri("/domain/sample/%s".formatted(domain))
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
