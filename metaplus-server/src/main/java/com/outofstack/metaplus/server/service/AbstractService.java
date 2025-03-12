package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.DateUtil;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.json.JsonRule;
import com.outofstack.metaplus.common.model.DocUtil;
import com.outofstack.metaplus.common.model.DomainDoc;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.server.MetaplusException;
import com.outofstack.metaplus.server.dao.DocDao;
import com.outofstack.metaplus.server.dao.IndexDao;
import com.outofstack.metaplus.server.dao.SearchDao;
import com.outofstack.metaplus.server.domain.DomainLib;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractService {

    @Autowired
    DomainLib domainLib;

    @Autowired
    IndexDao indexDao;

    @Autowired
    DocDao docDao;

    @Autowired
    SearchDao searchDao;


    void validateDoc(MetaplusDoc doc) {
        if (null == doc) {
            throw new IllegalArgumentException("Doc is null");
        }
        validateDomain(doc.getFqmnDomain());
    }

    void validateFqmn(String fqmn) {
        if (null == fqmn) {
            throw new IllegalArgumentException("Fqmn is null");
        }
        String[] ss = DocUtil.checkAndSplitFqmn(fqmn);
        validateDomain(ss[1]);
    }

    void validateDomain(String domain) {
        DomainDoc domainDoc = domainLib.getDomainDoc(domain);
        if (null == domainDoc) {
            throw new IllegalArgumentException("Domain '" + domain + "' does not exist");
        }
    }

    void validatePrivilege() {
        // TODO
    }

    void fixupSyncCreated(MetaplusDoc doc) {
        String createdAt = doc.getSyncCreatedAt();
        String newCreatedAt;
        if (null == createdAt || createdAt.isEmpty()) {
            newCreatedAt = DateUtil.formatNow();
        } else {
            try {
                newCreatedAt = DateUtil.epochMilli2Formatted(Long.parseLong(createdAt));
            } catch (RuntimeException e) {
                try {
                    newCreatedAt = DateUtil.format(DateUtil.tryParse(createdAt));
                } catch (RuntimeException e2) {
                    newCreatedAt = DateUtil.formatNow();
                }
            }
        }
        doc.setSyncCreatedAt(newCreatedAt);
        doc.deleteSyncUpdatedBy();
        doc.deleteSyncUpdatedAt();
    }

    void fixupSyncUpdated(MetaplusDoc doc) {
        String updatedAt = doc.getSyncUpdatedAt();
        String newUpdatedAt;
        if (null == updatedAt || updatedAt.isEmpty()) {
            newUpdatedAt = DateUtil.formatNow();
        } else {
            try {
                newUpdatedAt = DateUtil.epochMilli2Formatted(Long.parseLong(updatedAt));
            } catch (RuntimeException e) {
                try {
                    newUpdatedAt = DateUtil.format(DateUtil.tryParse(updatedAt));
                } catch (RuntimeException e2) {
                    newUpdatedAt = DateUtil.formatNow();
                }
            }
        }
        doc.setSyncUpdatedAt(newUpdatedAt);
        doc.deleteSyncCreatedBy();
        doc.deleteSyncCreatedAt();
    }

    void trim2MetaOnly(MetaplusDoc doc) {
        doc.setPlus(new JsonObject());
    }

    void trim2PlusOnly(MetaplusDoc doc) {
        doc.setMeta(new JsonObject());
    }

    void validateAndFixupByRules(MetaplusDoc doc) {
        List<JsonRule> docRules = domainLib.getRules(doc.getFqmnDomain());
        for (JsonRule jsonRule : docRules) {
            if (null != jsonRule.getDefault()) {
                if (null == doc.getByPath(jsonRule.getJsonPath())) {
                    doc.putByPath(jsonRule.getJsonPath(), jsonRule.getDefault());
                }
            } else if (jsonRule.getRequired()) {
                if (null == doc.getByPath(jsonRule.getJsonPath())) {
                    throw new MetaplusException("JsonRule validate fail, '" + jsonRule.getJsonPath() +
                            "' is required in doc");
                }
            }
        }
    }


}
