package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.DateUtil;
import com.outofstack.metaplus.common.json.JsonRule;
import com.outofstack.metaplus.common.model.*;
import com.outofstack.metaplus.server.MetaplusException;
import com.outofstack.metaplus.server.dao.DocDao;
import com.outofstack.metaplus.server.domain.DomainLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocService {

    @Autowired
    private DocDao docDao;

    @Autowired
    private DomainLib domainLib;

    public void metaCreate(MetaplusDoc doc) {
        // 1 validate param
        validateParam(doc);
        if (doc.getMeta().isEmpty()) {
            throw new MetaplusException("metaBody of Doc is empty.");
        }

        // 2 validate privilege
        validatePrivilege(doc);

        // 3 build doc
        if (docDao.exist(doc.getFqmn())) {
            throw new MetaplusException("Fqmn '" + doc.getFqmn() + "' already exist");
        }
        fixupCreateCts(doc);

        // 4 schema
        validateAndFixupSchema(doc);

        // 5 create doc
        docDao.create(doc);
    }


    public void metaUpdate(MetaplusPatch patch) {
        // 1 validate param
        validateParam(patch);
        if (PatchType.META_PATCH != patch.getPatchType()) {
            throw new IllegalArgumentException("In metaUpdate, the type of MetaplusPatch must be 'META_PATCH'.");
        }

        // 2 validate privilege
        validatePrivilege(patch);

        // 3 build doc
        if (!docDao.exist(patch.getFqmn())) {
            throw new MetaplusException("Fqmn '" + patch.getFqmn() + "' does not exist");
        }

        fixupUpdateCts(patch);
        patch.transformPatchBody2DocBody();
        MetaplusDoc doc = patch.toDoc();

        // 3 update doc
        docDao.update(doc);
    }

    public void plusUpdate(MetaplusPatch patch) {
        // 1 validate param
        validateParam(patch);
        if (PatchType.PLUS_PATCH != patch.getPatchType()) {
            throw new IllegalArgumentException("In plusUpdate, the type of MetaplusPatch must be 'PLUS_PATCH'.");
        }

        // 2 validate privilege
        validatePrivilege(patch);

        // 3 build doc
        if (!docDao.exist(patch.getFqmn())) {
            throw new MetaplusException("Fqmn '" + patch.getFqmn() + "' does not exist");
        }

        fixupUpdateCts(patch);
        patch.transformPatchBody2DocBody();
        MetaplusDoc doc = patch.toDoc();

        // 3 update doc
        docDao.update(doc);
    }

    public void metaDelete(MetaplusPatch patch) {
        // 1 validate param
        validateParam(patch);

        // 2 validate privilege
        validatePrivilege(patch);

        // 3 delete fqmn
        if (!docDao.exist(patch.getFqmn())) {
            throw new MetaplusException("Fqmn '" + patch.getFqmn() + "' does not exist");
        }
        docDao.delete(patch.getFqmn());
    }

    public MetaplusDoc read(String fqmn) {
        return docDao.read(fqmn);
    }

    public boolean exist(String fqmn) {
        return docDao.exist(fqmn);
    }

    private void validateParam(MetaplusDoc doc) {
        if (null == doc) {
            throw new IllegalArgumentException("Doc is null");
        }
        String domain = doc.getFqmnDomain();
        DomainDoc domainDoc = domainLib.getDomainDoc(domain);
        if (null == domainDoc) {
            throw new IllegalArgumentException("Domain '" + domain + "' does not exist");
        }

        if (!domainDoc.getBooleanByPath("$.meta.index.created")) {
            throw new IllegalArgumentException("Index of domain '" + domain + "' is not created");
        }
    }

    private void validatePrivilege(MetaplusDoc doc) {
        // TODO
    }


    private void fixupCreateCts(MetaplusDoc doc) {
        String createdAt = doc.getCtsCreatedAt();
        String newCreateAt;
        if (null == createdAt || createdAt.isEmpty()) {
            newCreateAt = DateUtil.formatNow();
        } else {
            try {
                newCreateAt = DateUtil.epochMilli2Formatted(Long.parseLong(createdAt));
            } catch (RuntimeException e) {
                try {
                    newCreateAt = DateUtil.format(DateUtil.tryParse(createdAt));
                } catch (RuntimeException e2) {
                    newCreateAt = DateUtil.formatNow();
                }
            }
        }
        doc.setCtsCreatedAt(newCreateAt);
        doc.deleteCtsUpdatedBy();
        doc.deleteCtsUpdatedAt();
    }

    private void fixupUpdateCts(MetaplusDoc doc) {
        String updatedAt = doc.getCtsUpdatedAt();
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
        doc.setCtsUpdatedAt(newUpdatedAt);
        doc.deleteCtsCreatedBy();
        doc.deleteCtsCreatedAt();
    }

    public void validateAndFixupSchema(MetaplusDoc doc) {
        List<JsonRule> docRules = domainLib.getRules(doc.getFqmnDomain());
        for (JsonRule jsonRule : docRules) {
            if (null != jsonRule.getDefault()) {
                if (null == doc.getByPath(jsonRule.getJsonPath())) {
                    doc.putByPath(jsonRule.getJsonPath(), jsonRule.getDefault());
                }
            } else if (jsonRule.getRequired()) {
                if (null == doc.getByPath(jsonRule.getJsonPath())) {
                    throw new MetaplusException("JsonRule validate fail, '" + jsonRule.getJsonPath() + "' is required");
                }
            }
        }
    }

}
