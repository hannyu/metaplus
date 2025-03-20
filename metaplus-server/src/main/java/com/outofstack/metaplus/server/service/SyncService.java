package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.model.MetaplusPatch;
import com.outofstack.metaplus.common.model.PatchMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncService {

    @Autowired
    private PatchService patchService;
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
            metaService.create(patch.getDoc());
        } else if (method == PatchMethod.META_UPDATE) {
            metaService.update(patch.getDoc());
        } else if (method == PatchMethod.META_DELETE) {
            metaService.delete(patch.getDoc());
        } else if (method == PatchMethod.PLUS_UPDATE) {
            plusService.update(patch.getDoc());
        } else if (method == PatchMethod.PATCH_RENAME) {
            patchService.rename(patch);
        } else if (method == PatchMethod.PATCH_UPDATE) {
            patchService.updateByQuery(patch);
        } else {
            throw new IllegalArgumentException("Unsupported PatchMethod '" + method + "'");
        }

    }

    private void validatePrivilege() {
        /// TODO
    }
}
