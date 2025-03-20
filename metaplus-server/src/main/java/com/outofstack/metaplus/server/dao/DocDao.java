package com.outofstack.metaplus.server.dao;

import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.*;
import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.common.model.search.Query;
import com.outofstack.metaplus.server.MetaplusException;
import com.outofstack.metaplus.server.storage.EsResponse;
import org.springframework.stereotype.Component;


@Component
public class DocDao extends AbstractDao {


    public void create(MetaplusDoc doc) {
        String index = domainLib.getAndCheckIndexName(doc.getFqmnDomain());
        String fqmn = doc.getFqmnFqmn();
        String url = "/%s/_doc/?refresh".formatted(index);

        EsResponse response = esClient.post(url, doc);
        if (!response.isSuccess()) {
            throw new MetaplusException("DocDao.create() '" + fqmn + "' fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
    }

    @Deprecated
    public void update0(MetaplusDoc doc) {
        String index = domainLib.getAndCheckIndexName(doc.getFqmnDomain());
        String fqmn = doc.getFqmnFqmn();
        String url = "/%s/_update/%s".formatted(index, fqmn);

        EsResponse response = esClient.post(url, new JsonObject().put("doc", doc));
        if (!response.isSuccess()) {
            throw new MetaplusException("DocDao.update() '" + fqmn + "' fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
    }

    public void update(MetaplusDoc doc) {
        String index = domainLib.getAndCheckIndexName(doc.getFqmnDomain());
        String fqmn = doc.getFqmnFqmn();
        String url = "/%s/_update_by_query?refresh".formatted(index);

        Query query = new Query();
        query.setQuery(new JsonObject("term", new JsonObject("fqmn.fqmn", fqmn)));
        query.setScript(new JsonObject()
                .put("source", SCRIPT_ALL_IN_ONE + domainLib.getExpressions(doc.getFqmnDomain()))
                .put("params", new JsonObject()
                        .put("sync", doc.getSync())
                        .put("meta", doc.getMeta())
                        .put("plus", doc.getPlus())
                )
        );

        EsResponse response = esClient.post(url, query);
        if (!response.isSuccess()) {
            throw new MetaplusException("DocDao.update() '" + fqmn + "' fail, response code:" + response.getStatusCode() +
                    " and body: " + response.getBody());
        }
        if (response.getBody().getInteger("updated") == 0) {
            throw new MetaplusException("DocDao.update() '" + fqmn + "' fail, nothing updated, and body: " +
                    response.getBody());
        }
    }

    @Deprecated
    public void delete0(String fqmn) {
        String[] ss = ModelUtil.checkAndSplitFqmn(fqmn);
        String index = domainLib.getAndCheckIndexName(ss[1]);
        String url = "/%s/_doc/%s".formatted(index, fqmn);

        EsResponse response = esClient.delete(url);
        if (!response.isSuccess()) {
            throw new MetaplusException("DocDao.delete() '" + fqmn + "' fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
    }

    public void delete(String fqmn) {
        String[] ss = ModelUtil.checkAndSplitFqmn(fqmn);
        String index = domainLib.getAndCheckIndexName(ss[1]);
        String url = "/%s/_update_by_query?refresh".formatted(index);

        Query query = new Query();
        query.setQuery(new JsonObject("term", new JsonObject("fqmn.fqmn", fqmn)));
        query.setScript(new JsonObject("source", " ctx.op = 'delete'"));

        EsResponse response = esClient.post(url, query);
        if (!response.isSuccess()) {
            throw new MetaplusException("DocDao.delete() '" + fqmn + "' fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
        if (response.getBody().getInteger("deleted") == 0) {
            throw new MetaplusException("DocDao.delete() '" + fqmn + "' fail, nothing deleted.");
        }
    }


    public void rename(MetaplusPatch patch) {
        String index = domainLib.getAndCheckIndexName(patch.getDomain());
        String url = "/%s/_update_by_query?refresh".formatted(index);
        MetaplusDoc doc = patch.getDoc();

        Query query = new Query();
        query.setQuery(new JsonObject("term", new JsonObject("fqmn.fqmn", doc.getFqmnFqmn())));
        query.setScript(new JsonObject()
                .put("source", SCRIPT_ALL_IN_ONE +
                        " renameFqmn(fqmn, params.fqmn); merge(meta, params.meta);")
                .put("params", new JsonObject()
                        .put("fqmn", patch.getPatch().getJsonObject("rename").getJsonObject("fqmn"))
                        .put("sync", doc.getSync())
                        .put("meta", doc.getMeta())

                )
        );

        EsResponse response = esClient.post(url, query);
        if (!response.isSuccess()) {
            throw new MetaplusException("DocDao.rename() '" + doc.getFqmn() + "' fail, response code:" + response.getStatusCode() +
                    " and body: " + response.getBody());
        }
        if (response.getBody().getInteger("updated") == 0) {
            throw new MetaplusException("DocDao.rename() '" + doc.getFqmn() + "' fail, nothing updated, and body: " +
                    response.getBody());
        }
    }

    public void updateByQuery(MetaplusPatch patch) {
        String index = domainLib.getAndCheckIndexName(patch.getDomain());
        String url = "/%s/_update_by_query?refresh".formatted(index);

        JsonObject script = patch.getPatch().getJsonObject("script");
        String source = script.getString("source");
        script.put("source", SCRIPT_ALL_IN_ONE + (source == null ? "" : source) +
                domainLib.getExpressions(patch.getDomain()));

        EsResponse response = esClient.post(url, patch.getPatch());
        if (!response.isSuccess()) {
            throw new MetaplusException("DocDao.updateByQuery() '" + patch.getDomain() + "' fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
    }

    public boolean exist(String fqmn) {
        String[] ss = ModelUtil.checkAndSplitFqmn(fqmn);
        return exist(ss[0], ss[1], ss[2]);
    }

    @Deprecated
    public boolean exist0(String corp, String domain, String name) {
        String index = domainLib.getAndCheckIndexName(domain);
        String fqmn = ModelUtil.packFqmn(corp, domain, name);
        String url = "/%s/_doc/%s".formatted(index, fqmn);

        EsResponse response = esClient.head(url);
        return response.isSuccess();
    }

    public boolean exist(String corp, String domain, String name) {
        String index = domainLib.getAndCheckIndexName(domain);
        String fqmn = ModelUtil.packFqmn(corp, domain, name);
        String url = "/%s/_search?size=0&terminate_after=1".formatted(index);

        Query query = new Query();
        query.setQuery(new JsonObject("term", new JsonObject("fqmn.fqmn", fqmn)));

        EsResponse response = esClient.post(url, query);
        if (!response.isSuccess()) {
            throw new MetaplusException("DocDao.exit() '" + fqmn + "' fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
        Integer totalValue = response.getBody().getIntegerByPath("$.hits.total.value");
        return totalValue > 0;
    }

    public MetaplusDoc read(String fqmn) {
        String[] ss = ModelUtil.checkAndSplitFqmn(fqmn);
        return read(ss[0], ss[1], ss[2]);
    }

    public MetaplusDoc read(String corp, String domain, String name) {
        String index = domainLib.getAndCheckIndexName(domain);
        String fqmn = ModelUtil.packFqmn(corp, domain, name);
        String url = "/%s/_search?version=true".formatted(index);

        Query query = new Query();
        query.setQuery(new JsonObject("term", new JsonObject("fqmn.fqmn", fqmn)));

        EsResponse response = esClient.get(url, query);
        if (response.isNotFound()) {
            return null;
        } else if (response.isSuccess()) {
            Hits hits = new Hits(response.getBody());
            if (hits.getHitsSize() > 0) {
                return hits.getHitsDoc(0);
            } else {
                return null;
            }
        } else {
            throw new MetaplusException("DocDao.read() '" + fqmn + "' fail, response code:" + response.getStatusCode() +
                    " and body:" + response.getBody());
        }
    }


}
