package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.model.MetaplusDoc;
import org.springframework.stereotype.Component;

@Component
public class DocService extends AbstractService {

    public MetaplusDoc readDoc(String fqmn) {
        // 1 validate param
        validateFqmn(fqmn);

        // 2 validate privilege
        validatePrivilege();

        // 3 read doc
        return docDao.read(fqmn);
    }

}
