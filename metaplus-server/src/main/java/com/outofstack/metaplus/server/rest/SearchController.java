package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.server.service.DocService;
import com.outofstack.metaplus.server.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/search")
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/simple/{domains}/{queryText}")
    public HttpResponse<Hits> simple(@PathVariable String domains, @PathVariable String queryText) {
        Hits hits = searchService.simpleSearch(domains, queryText);
        return new HttpResponse<Hits>(200, hits);
    }


}
