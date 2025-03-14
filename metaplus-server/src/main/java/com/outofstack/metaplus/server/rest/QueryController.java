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

    @RequestMapping("/exist_doc/{fqmn}")
    public ResponseEntity<HttpResponse<JsonObject>> existDoc(@PathVariable String fqmn) {
        MetaplusDoc doc = new MetaplusDoc(fqmn);
        if (queryService.existDoc(doc)) {
            return ResponseEntity.ok().body(HttpResponse.ok());
        } else {
            return ResponseEntity.status(404).body(HttpResponse.notFound());
        }
    }

    @GetMapping("/read_doc/{fqmn}")
    public HttpResponse<MetaplusDoc> readDoc(@PathVariable String fqmn) {
        MetaplusDoc doc = queryService.readDoc(fqmn);
        return new HttpResponse<MetaplusDoc>(200, doc);
    }

    @GetMapping("/simple_search/{domains}/{queryText}")
    public HttpResponse<Hits> simpleSearch(@PathVariable String domains, @PathVariable String queryText) {
        Hits hits = queryService.simpleSearch(domains, queryText);
        return new HttpResponse<Hits>(200, hits);
    }


}
