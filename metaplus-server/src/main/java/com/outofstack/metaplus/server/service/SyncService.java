package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.model.MetaplusPatch;
import com.outofstack.metaplus.common.model.PatchMethod;
import com.outofstack.metaplus.server.MetaplusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncService {

    @Autowired
    private MetaService metaService;

    @Autowired
    private PlusService plusService;


    public void syncOne(MetaplusPatch patch) {
        // 1 validate privilege
        validatePrivilege();

        // 2 dispatch
        PatchMethod method = patch.getMethod();
        if (method == PatchMethod.META_CREATE) {
            metaService.createMeta(patch);
        } else if (method == PatchMethod.META_UPDATE) {
            metaService.updateMeta(patch);
        } else if (method == PatchMethod.META_DELETE) {
            metaService.deleteMeta(patch);
        } else if (method == PatchMethod.PLUS_UPDATE) {
            plusService.updatePlus(patch);
        } else {
            throw new IllegalArgumentException("Not supported PatchMethod '" + method + "'");
        }

    }

    private void validatePrivilege() {
        /// TODO
    }
}
