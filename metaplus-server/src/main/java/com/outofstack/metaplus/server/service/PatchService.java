package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.*;
import com.outofstack.metaplus.common.model.search.Query;
import com.outofstack.metaplus.server.MetaplusException;
import org.springframework.stereotype.Component;

@Component
public class PatchService extends AbstractService{

//    public void rename0(MetaplusPatch patch) {
//        // 1 validate param
//        validateDomain(patch.getDomain());
//
//        // 2 validate privilege
//        validatePrivilege();
//
//        // 3 rename
//        String newCorp = null;
//        String newDomain = null;
//        String newName = null;
//        JsonObject newFqmn = patch.getPatch().getJsonObject("newFqmn");
//        if (null != newFqmn) {
//            newCorp = newFqmn.getString("corp");
//            newDomain = newFqmn.getString("domain");
//            newName = newFqmn.getString("name");
//        }
//        if (null == newCorp && null == newDomain && null == newName) {
//            throw new MetaplusException("newFqmn is empty.");
//        }
//
//        // 4 read doc
//        MetaplusDoc doc = patch.getDoc();
//        MetaplusDoc newDoc = docDao.read(doc.getFqmnFqmn());
//        if (null == newDoc) {
//            throw new MetaplusException("Meta '" + doc.getFqmn() + "' does not exist.");
//        }
//        newDoc.setFqmn(newCorp, newDomain, newName);
//
//        // 5 create newDoc
//        newDoc.getMeta().merge(doc.getMeta());
//        newDoc.getPlus().merge(doc.getPlus());
//        fixupSyncUpdated(newDoc);
//        docDao.create(newDoc);
//
//        // 6 delete old
//        docDao.delete(doc.getFqmnFqmn());
//
//    }


    public void rename(MetaplusPatch patch) {
        // 1 validate param
        validateDomain(patch.getDomain());

        // 2 validate privilege
        validatePrivilege();

        // 3 build
        MetaplusDoc doc = patch.getDoc();
        if (null == doc) {
            throw new MetaplusException("Giving patch must have doc.");
        }
        if (!docDao.exist(doc.getFqmnFqmn())) {
            throw new MetaplusException("Meta '" + doc.getFqmn() + "' does not exist");
        }
        fixupSyncUpdated(doc);

        // 4 rename
        docDao.rename(patch);
    }



    public void updateByQuery(MetaplusPatch patch) {
        // 1 validate param
        validateDomain(patch.getDomain());

        // 2 validate privilege
        validatePrivilege();

        // 3 have patch
        if (null == patch.getPatch()) {
            throw new MetaplusException("Patch must have 'patch'.");
        }
        Query query = new Query(patch.getPatch());
        JsonObject script = query.getScript();
//        if (null == script.getString("source")) {
//            throw new MetaplusException("Patch must have 'patch.script.source'.");
//        }
        String updateAt = script.getStringByPath("$.params.sync.updatedAt");
        script.putByPath("$.params.sync.updatedAt", fixupAt(updateAt));

        // 4 update_by_query
        docDao.updateByQuery(patch);
    }

}
