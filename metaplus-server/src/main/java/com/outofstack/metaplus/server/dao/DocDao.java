package com.outofstack.metaplus.server.dao;

import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.*;
import com.outofstack.metaplus.server.MetaplusException;
import com.outofstack.metaplus.server.domain.DomainLib;
import com.outofstack.metaplus.server.storage.EsClient;
import com.outofstack.metaplus.server.storage.EsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DocDao {

    @Autowired
    private EsClient esClient;

    @Autowired
    private DomainLib domainLib;

    /**
     * 只做逻辑检查，不做内容检查
     *
     * @param doc
     */
    public void create(MetaplusDoc doc) {
        String index = domainLib.getAndCheckIndexName(doc.getFqmnDomain());
        String fqmn = doc.getFqmn();
        String url = "/%s/_create/%s".formatted(index, fqmn);

        EsResponse response = esClient.put(url, doc);
        if (!response.isSuccess()) {
            throw new MetaplusException("create '" + fqmn + "' fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
    }

    public void update(MetaplusDoc doc) {
        String index = domainLib.getAndCheckIndexName(doc.getFqmnDomain());
        String fqmn = doc.getFqmn();
        String url = "/%s/_update/%s".formatted(index, fqmn);

        EsResponse response = esClient.post(url, new JsonObject().put("doc", doc));
        if (!response.isSuccess()) {
            throw new MetaplusException("update '" + fqmn + "' fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
    }

    public void delete(String fqmn) {
        String[] ss = DocUtil.checkAndSplitFqmn(fqmn);
        String index = domainLib.getAndCheckIndexName(ss[1]);
        String url = "/%s/_doc/%s".formatted(index, fqmn);

        EsResponse response = esClient.delete(url);
        if (!response.isSuccess()) {
            throw new MetaplusException("delete '" + fqmn + "' fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
    }

    public MetaplusDoc read(String fqmn) {
        String[] ss = DocUtil.checkAndSplitFqmn(fqmn);
        return read(ss[0], ss[1], ss[2]);
    }

    public MetaplusDoc read(String corp, String domain, String name) {
        String index = domainLib.getAndCheckIndexName(domain);
        String fqmn = DocUtil.combineFqmn(corp, domain, name);
        String url = "/%s/_doc/%s".formatted(index, fqmn);

        EsResponse response = esClient.get(url);
        if (!response.isSuccess()) {
            throw new MetaplusException("read '" + fqmn + "' fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
        MetaplusDoc doc = new MetaplusDoc(response.getBody().getJsonObject("_source"));
        doc.setCtsVersion(response.getBody().getInteger("_version"));
        return doc;
    }


    public boolean exist(String fqmn) {
        String[] ss = DocUtil.checkAndSplitFqmn(fqmn);
        return exist(ss[0], ss[1], ss[2]);
    }

    public boolean exist(String corp, String domain, String name) {
        String index = domainLib.getAndCheckIndexName(domain);
        String fqmn = DocUtil.combineFqmn(corp, domain, name);
        String url = "/%s/_doc/%s".formatted(index, fqmn);

        EsResponse response = esClient.head(url);
        return response.isSuccess();
    }

    /**
     * FIXME: need more check
     *
     * @param patch
     */
    private void checkValid(MetaplusPatch patch) {
        if (null == patch) {
            throw new IllegalArgumentException("patch can not be null");
        }
    }



}
