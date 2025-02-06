package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.common.model.search.Query;
import com.outofstack.metaplus.server.dao.SearchDao;
import com.outofstack.metaplus.server.domain.DomainLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SearchService {

    @Autowired
    private SearchDao searchDao;

    @Autowired
    private DomainLib domainLib;


    public Hits simpleSearch(String queryStr) {
        return simpleSearch((List<String>)null, queryStr);
    }

    public Hits simpleSearch(String domain, String queryStr) {
        return simpleSearch(Collections.singletonList(domain), queryStr);
    }

    public Hits simpleSearch(List<String> domains, String queryStr) {
        if (null == domains || domains.isEmpty()) {
            domains = domainLib.listCustomDomains();
        }

        Query query = new Query();
        query.setQuery(new JsonObject("simple_query_string", new JsonObject("query", queryStr)));
        return searchDao.query(domains, query);
    }




}
