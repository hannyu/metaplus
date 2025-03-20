package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusPatch;
import com.outofstack.metaplus.server.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/sync")
@RestController
public class SyncController {

    @Autowired
    private SyncService syncService;

    @PostMapping({"/one", "/one/{domain}"})
    public HttpResponse<JsonObject> one(@PathVariable(name = "domain", required = false) String domain,
                                        @RequestBody JsonObject requestBody) {
        MetaplusPatch patch = new MetaplusPatch(requestBody);
        if (null != domain && !domain.isEmpty() && !domain.equals(patch.getDomain())) {
            throw new IllegalArgumentException("Domain '" + domain + "' in path is NOT equal to domain '" +
                    patch.getDomain() + "' in patch.");
        }
        syncService.syncOne(patch);
        return HttpResponse.ok();
    }

}
