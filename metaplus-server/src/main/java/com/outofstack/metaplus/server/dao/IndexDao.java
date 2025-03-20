package com.outofstack.metaplus.server.dao;

import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.server.MetaplusException;
import com.outofstack.metaplus.server.storage.EsClient;
import com.outofstack.metaplus.server.storage.EsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 索引的CRUD基本操作，将来可扩展到其他性能、观察等API
 */
@Component
public class IndexDao extends AbstractDao {

    /**
     * if the index exist or not
     *
     * @param indexName
     * @return
     */
    public boolean existIndex(String indexName) {
        String url = "/%s".formatted(indexName);
        EsResponse response = esClient.head(url);
        return response.isSuccess();
    }

    /**
     * create physically
     *
     * @param indexName
     * @param schema
     */
    public void createIndex(String indexName, JsonObject schema) {
        String url = "/%s".formatted(indexName);
        EsResponse response = esClient.put(url, schema);
        if (!response.isSuccess()) {
            throw new MetaplusException("createIndex '" + indexName + "' fail, response code:" +
                    response.getStatusCode() + " and body:" + response.getBody());
        }
    }

    public JsonObject readIndex(String indexName) {
        String url = "/%s".formatted(indexName);
        EsResponse response = esClient.get(url);
        if (! response.isSuccess()) {
            throw new MetaplusException("readIndex '" + indexName + "' fail, response code:" +
                    response.getStatusCode() + " and body:" + response.getBody());
        }
        return response.getBody();
    }

    public void updateSettings(String indexName, JsonObject settings) {
        updateIndex(indexName, "_settings", settings);
    }

    public void updateMappings(String indexName, JsonObject mappings) {
        updateIndex(indexName, "_mapping", mappings);
    }

    /**
     * PUT {indexName}/{updateKey}
     * ex: PUT test001/_mapping
     *
     * @param indexName
     * @param updateKey
     * @param updateBody
     */
    private void updateIndex(String indexName, String updateKey, JsonObject updateBody) {
        String url = "/%s/%s".formatted(indexName, updateKey);
        EsResponse response = esClient.put(url, updateBody);
        if (!response.isSuccess()) {
            throw new MetaplusException("updateIndex '" + url + "' fail, response code:" +
                    response.getStatusCode() + " and body:" + response.getBody());
        }
    }

    public void deleteIndex(String indexName) {
        String url = "/%s".formatted(indexName);
        EsResponse response = esClient.delete(url);
        if (!response.isSuccess()) {
            throw new MetaplusException("deleteIndex '" + indexName + "' fail, response code:" +
                    response.getStatusCode() + " and body:" + response.getBody());
        }
    }


}
