package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.MetaplusPatch;
import com.outofstack.metaplus.server.service.PatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/patch")
@RestController
public class PatchController {

    @Autowired
    private PatchService patchService;

    @PostMapping("/update")
    public HttpResponse<JsonObject> update(@PathVariable(name = "domain", required = false) String domain,
                                        @RequestBody JsonObject requestBody) {
        MetaplusPatch patch = new MetaplusPatch(requestBody);
        if (null != domain && !domain.isEmpty() && !domain.equals(patch.getDomain())) {
            throw new IllegalArgumentException("Domain '" + domain + "' in url is NOT equal to domain '" +
                    patch.getDomain() + "' in patch.");
        }
        patchService.update(patch);
        return HttpResponse.ok();
    }

    @PostMapping("/delete")
    public HttpResponse<JsonObject> delete(@PathVariable(name = "domain", required = false) String domain,
                                           @RequestBody JsonObject requestBody) {
        MetaplusPatch patch = new MetaplusPatch(requestBody);
        if (null != domain && !domain.isEmpty() && !domain.equals(patch.getDomain())) {
            throw new IllegalArgumentException("Domain '" + domain + "' in url is NOT equal to domain '" +
                    patch.getDomain() + "' in patch.");
        }
        patchService.delete(patch);
        return HttpResponse.ok();
    }

    @PostMapping("/script")
    public HttpResponse<JsonObject> script(@PathVariable(name = "domain", required = false) String domain,
                                           @RequestBody JsonObject requestBody) {
        MetaplusPatch patch = new MetaplusPatch(requestBody);
        if (null != domain && !domain.isEmpty() && !domain.equals(patch.getDomain())) {
            throw new IllegalArgumentException("Domain '" + domain + "' in url is NOT equal to domain '" +
                    patch.getDomain() + "' in patch.");
        }
        patchService.script(patch);
        return HttpResponse.ok();
    }


}
