package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.model.*;
import com.outofstack.metaplus.server.MetaplusException;
import org.springframework.stereotype.Component;

@Component
public class PatchService extends AbstractService{

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

    public void updatePlus(MetaplusDoc doc) {
        // 1 validate param
        validateDoc(doc);

        // 2 validate privilege
        validatePrivilege();

        // 3 build doc
        if (!docDao.exist(doc.getFqmn())) {
            throw new MetaplusException("Meta '" + doc.getFqmn() + "' does not exist");
        }
        fixupSyncUpdated(doc);
        trim2PlusOnly(doc);

        // 3 update doc
        docDao.update(doc);
    }

    public void rename(MetaplusPatch patch) {

    }

    public void script(MetaplusPatch patch) {

    }

}
