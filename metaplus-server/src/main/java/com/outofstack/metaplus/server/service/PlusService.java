package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.server.MetaplusException;
import org.springframework.stereotype.Component;

@Component
public class PlusService extends AbstractService {

    public void updatePlus(MetaplusDoc doc) {
        // 1 validate param
        validateDoc(doc);

        // 2 validate privilege
        validatePrivilege();

        // 3 build doc
        if (!docDao.exist(doc.getFqmn())) {
            throw new MetaplusException("Meta '" + doc.getFqmn() + "' does not exist");
        }
        fixupCtsUpdated(doc);
        trim2PlusOnly(doc);

        // 3 update doc
        docDao.update(doc);
    }

}
