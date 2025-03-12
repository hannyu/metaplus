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
public class MetaService extends AbstractService{

    public void createMeta(MetaplusDoc doc) {
        // 1 validate param
        validateDoc(doc);

        // 2 validate privilege
        validatePrivilege();

        // 3 build doc
        if (docDao.exist(doc.getFqmn())) {
            throw new MetaplusException("Meta '" + doc.getFqmn() + "' is already exist");
        }
        fixupSyncCreated(doc);
        trim2MetaOnly(doc);

        // 4 schema
        validateAndFixupByRules(doc);

        // 5 create doc
        docDao.create(doc);
    }

    public void updateMeta(MetaplusDoc doc) {
        // 1 validate param
        validateDoc(doc);

        // 2 validate privilege
        validatePrivilege();

        // 3 build doc
        if (!docDao.exist(doc.getFqmn())) {
            throw new MetaplusException("Meta '" + doc.getFqmn() + "' does not exist");
        }
        fixupSyncUpdated(doc);
        trim2MetaOnly(doc);

        // 3 update doc
        docDao.update(doc);
    }

    public void deleteMeta(MetaplusDoc doc) {
        // 1 validate param
        validateDoc(doc);

        // 2 validate privilege
        validatePrivilege();

        // 3 build doc
        if (!docDao.exist(doc.getFqmn())) {
            throw new MetaplusException("Meta '" + doc.getFqmn() + "' does not exist");
        }

        // 4 delete doc
        docDao.delete(doc.getFqmn());
    }

    public boolean existMeta(MetaplusDoc doc) {
        // 1 validate param
        validateDoc(doc);

        // 2 validate privilege
        validatePrivilege();

        // 3 exist doc
        return docDao.exist(doc.getFqmn());
    }


}
