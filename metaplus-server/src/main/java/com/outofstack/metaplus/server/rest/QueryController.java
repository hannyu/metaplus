package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.server.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/query")
@RestController
public class QueryController {

    @Autowired
    private QueryService queryService;


    @RequestMapping("/exist/{fqmn}")
    public ResponseEntity<HttpResponse<JsonObject>> exist(@PathVariable String fqmn) {
        if (queryService.exist(fqmn)) {
            return ResponseEntity.ok().body(HttpResponse.ok());
        } else {
            // because 'notFound' can not have body
            return ResponseEntity.status(404).body(HttpResponse.notFound());
        }
    }

    @GetMapping("/read/{fqmn}")
    public ResponseEntity<HttpResponse<JsonObject>> read(@PathVariable String fqmn) {
        MetaplusDoc doc = queryService.read(fqmn);
        if (null == doc) {
            return ResponseEntity.status(404).body(HttpResponse.notFound());
        } else {
            return ResponseEntity.ok().body(new HttpResponse<JsonObject>(200, doc));
        }
    }

    @GetMapping("/simple_search/{domains}/{queryText}")
    public HttpResponse<Hits> simpleSearch(@PathVariable String domains, @PathVariable String queryText) {
        Hits hits = queryService.simpleSearch(domains, queryText);
        return new HttpResponse<Hits>(200, hits);
    }


}
