package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.model.MetaplusPatch;
import com.outofstack.metaplus.common.model.PatchMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncService {

    @Autowired
    private PatchService patchService;


    public void syncOne(MetaplusPatch patch) {
        // 1 validate privilege
        validatePrivilege();

        // 2 dispatch
        PatchMethod method = patch.getMethod();
        if (method == PatchMethod.META_CREATE) {
            patchService.createMeta(patch);
        } else if (method == PatchMethod.META_UPDATE) {
            patchService.updateMeta(patch);
        } else if (method == PatchMethod.META_DELETE) {
            patchService.deleteMeta(patch);
        } else if (method == PatchMethod.PLUS_UPDATE) {
            patchService.updatePlus(patch);
        } else {
            throw new IllegalArgumentException("Unsupported PatchMethod '" + method + "'");
        }

    }

    private void validatePrivilege() {
        /// TODO
    }
}
