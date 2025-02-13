package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.common.model.search.Query;
import com.outofstack.metaplus.server.dao.SearchDao;
import com.outofstack.metaplus.server.domain.DomainLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SearchService extends AbstractService {

    public Hits simpleSearch(String domains, String queryText) {
        if (null == domains || domains.isEmpty() || domains.equals("*")) {
            return simpleSearch((List<String>) null, queryText);
        } else {
            return simpleSearch(Arrays.asList(domains.split(",")), queryText);
        }
    }

    public Hits simpleSearch(List<String> domains, String queryText) {
        if (null == queryText || queryText.isEmpty()) {
            throw new IllegalArgumentException("Search queryText can not be empty");
        }
        if (null == domains || domains.isEmpty()) {
            domains = domainLib.listCustomDomains();
        }

        Query query = new Query();
        query.setQuery(new JsonObject("simple_query_string", new JsonObject("query", queryText)));
        return searchDao.query(domains, query);
    }




}
