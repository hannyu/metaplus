package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.common.model.search.Query;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class QueryService extends AbstractService {


    public boolean exist(String fqmn) {
        // 1 validate param
        validateFqmn(fqmn);

        // 2 validate privilege
        validatePrivilege();

        // 3 exist doc
        return docDao.exist(fqmn);
    }


    public MetaplusDoc read(String fqmn) {
        // 1 validate param
        validateFqmn(fqmn);
        // 2 validate privilege
        validatePrivilege();
        // 3 read doc
        return docDao.read(fqmn);
    }


    public Hits simpleSearch(String domains, String searchText) {
        List<String> domainList = validateDomainsAnd2List(domains);
        if (null == searchText || searchText.isEmpty()) {
            throw new IllegalArgumentException("Search searchText is empty.");
        }

        Query query = new Query();
        query.setQuery(new JsonObject("simple_query_string", new JsonObject("query", searchText)));
        return searchDao.query(domainList, query);
    }


    public Hits search(String domains, Query query) {
        List<String> domainList = validateDomainsAnd2List(domains);
        validateQuery(query);

        return searchDao.query(domainList, query);
    }


    private void validateQuery(Query query) {
        if (null == query || query.getQuery().isEmpty()) {
            throw new IllegalArgumentException("Search query is empty.");
        }
    }

}
