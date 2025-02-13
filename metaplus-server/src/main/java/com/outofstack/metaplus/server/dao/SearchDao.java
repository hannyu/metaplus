package com.outofstack.metaplus.server.dao;

import com.outofstack.metaplus.common.StringUtil;
import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.common.model.search.Query;
import com.outofstack.metaplus.server.MetaplusException;
import com.outofstack.metaplus.server.domain.DomainLib;
import com.outofstack.metaplus.server.storage.EsClient;
import com.outofstack.metaplus.server.storage.EsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Component
public class SearchDao {

    @Autowired
    private EsClient esClient;

    @Autowired
    private DomainLib domainLib;

//    public Hits query(String domain, Query query) {
//        List<String> domains = new ArrayList<String>(1);
//        domains.add(domain);
//        return query(domains, query);
//    }

    public Hits query(List<String> domains, Query query) {
        List<String> indexNames = batchDomain2IndexName(domains);
        String url = "/%s/_search?version=true".formatted(StringUtil.joinString(indexNames, ","));

        EsResponse response = esClient.get(url, query);
        if (!response.isSuccess()) {
            throw new MetaplusException("query fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
        return new Hits(response.getBody());
    }

    private List<String> batchDomain2IndexName(List<String> domains) {
        return domains.stream().map((domain) -> domainLib.getAndCheckIndexName(domain)).toList();
    }

}
