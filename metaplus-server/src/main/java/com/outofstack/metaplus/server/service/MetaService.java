package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.MetaplusPatch;
import com.outofstack.metaplus.server.MetaplusException;
import org.springframework.stereotype.Component;

@Component
public class MetaService extends AbstractService{

    public void create(MetaplusDoc doc) {
        // 1 validate param
        validateDoc(doc);

        // 2 validate privilege
        validatePrivilege();

        // 3 build doc
        if (docDao.exist(doc.getFqmnFqmn())) {
            throw new MetaplusException("Meta '" + doc.getFqmn() + "' is already exist");
        }
        fixupSyncCreated(doc);
        trim2MetaOnly(doc);

        // 4 schema
        validateAndFixupByRules(doc);

        // 5 create doc
        docDao.create(doc);
    }

    public void update(MetaplusDoc doc) {
        // 1 validate param
        validateDoc(doc);

        // 2 validate privilege
        validatePrivilege();

        // 3 build doc
        if (!docDao.exist(doc.getFqmnFqmn())) {
            throw new MetaplusException("Meta '" + doc.getFqmn() + "' does not exist");
        }
        fixupSyncUpdated(doc);
        trim2MetaOnly(doc);

        // 3 update doc
        docDao.update(doc);
    }


    public void delete(MetaplusDoc doc) {
        // 1 validate param
        validateDoc(doc);

        // 2 validate privilege
        validatePrivilege();

        // 3 build doc
//        if (!docDao.exist(doc.getFqmnFqmn())) {
//            throw new MetaplusException("Meta '" + doc.getFqmnFqmn() + "' does not exist");
//        }

        // 4 delete doc
        docDao.delete(doc.getFqmnFqmn());
    }


}
