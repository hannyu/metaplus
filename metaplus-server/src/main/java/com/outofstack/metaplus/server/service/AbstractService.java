package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.TimeUtil;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.json.JsonRule;
import com.outofstack.metaplus.common.model.DocUtil;
import com.outofstack.metaplus.common.model.DomainDoc;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.server.MetaplusException;
import com.outofstack.metaplus.server.dao.DocDao;
import com.outofstack.metaplus.server.dao.IndexDao;
import com.outofstack.metaplus.server.dao.SearchDao;
import com.outofstack.metaplus.server.dao.DomainLib;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
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
            throw new IllegalArgumentException("Doc is null.");
        }
        validateDomain(doc.getFqmnDomain());
    }

    void validateFqmn(String fqmn) {
        if (null == fqmn || fqmn.isEmpty()) {
            throw new IllegalArgumentException("Fqmn is empty.");
        }
        String[] ss = DocUtil.checkAndSplitFqmn(fqmn);
        validateDomain(ss[1]);
    }

    void validateDomain(String domain) {
        DomainDoc domainDoc = domainLib.getDomainDoc(domain);
        if (null == domainDoc) {
            throw new IllegalArgumentException("Domain '" + domain + "' does not exist.");
        }
    }

    List<String> validateDomainsAnd2List(String domains) {
        if (null == domains || domains.isEmpty()) {
            throw new IllegalArgumentException("Domains is empty.");
        }
        if (domains.equals("*")) {
            return domainLib.listCustomDomains();
        }
        List<String> domainList = Arrays.asList(domains.split(","));
        for (String domain : domainList) {
            validateDomain(domain);
        }
        return domainList;
    }

    void validatePrivilege() {
        // TODO
    }

    String fixupAt(String at) {
        if (null == at || at.isEmpty()) {
            return  TimeUtil.formatNow();
        } else {
            try {
                return TimeUtil.epochMilli2Formatted(Long.parseLong(at));
            } catch (RuntimeException e) {
                try {
                    return TimeUtil.format(TimeUtil.tryParse(at));
                } catch (RuntimeException e2) {
                    return TimeUtil.formatNow();
                }
            }
        }
    }

    void fixupSyncCreated(MetaplusDoc doc) {
        doc.setSyncCreatedAt(fixupAt(doc.getSyncCreatedAt()));
        doc.deleteSyncUpdatedBy();
        doc.deleteSyncUpdatedAt();
    }

    void fixupSyncUpdated(MetaplusDoc doc) {
        doc.setSyncUpdatedAt(fixupAt(doc.getSyncUpdatedAt()));
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
            if (null != jsonRule.getDefaultValue()) {
                if (!doc.containsByPath(jsonRule.getJsonPath())) {
                    doc.putByPath(jsonRule.getJsonPath(), jsonRule.getDefaultValue());
                }
            } else if (jsonRule.getRequired()) {
                if (!doc.containsByPath(jsonRule.getJsonPath())) {
                    throw new MetaplusException("JsonRule validate fail, '" + jsonRule.getJsonPath() +
                            "' is required in doc");
                }
            }
        }
    }


}
