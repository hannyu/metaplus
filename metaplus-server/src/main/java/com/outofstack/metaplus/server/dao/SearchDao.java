package com.outofstack.metaplus.server.dao;

import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.common.model.search.Query;
import com.outofstack.metaplus.server.MetaplusException;
import com.outofstack.metaplus.server.storage.EsResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchDao extends AbstractDao {


    public Hits query(List<String> domains, Query query) {
        List<String> indexNames = batchDomain2IndexName(domains);
        String url = "/%s/_search?version=true".formatted(String.join(",", indexNames));

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
