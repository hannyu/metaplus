package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.server.service.PatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/patch")
@RestController
public class PatchController {

    @Autowired
    private PatchService patchService;

    @PutMapping("/create_meta/{fqmn}")
    public HttpResponse<JsonObject> createMeta(@PathVariable String fqmn, @RequestBody JsonObject requestBody) {
        MetaplusDoc doc = new MetaplusDoc(requestBody);
        if (null != fqmn && !fqmn.isEmpty()) {
            if (!doc.getFqmn().equals(fqmn)) {
                throw new IllegalArgumentException("Fqmn '" + fqmn + "' in path is NOT equal to fqmn '" +
                        doc.getFqmn() + "' in doc.");
            }
        }
        patchService.createMeta(doc);
        return HttpResponse.ok();
    }

    @PostMapping("/update_meta/{fqmn}")
    public HttpResponse<JsonObject> updateMeta(@PathVariable String fqmn, @RequestBody JsonObject requestBody) {
        MetaplusDoc doc = new MetaplusDoc(requestBody);
        if (null != fqmn && !fqmn.isEmpty()) {
            if (!doc.getFqmn().equals(fqmn)) {
                throw new IllegalArgumentException("Fqmn '" + fqmn + "' in path is NOT equal to fqmn '" +
                        doc.getFqmn() + "' in doc.");
            }
        }
        patchService.updateMeta(doc);
        return HttpResponse.ok();
    }

    @DeleteMapping("/delete_meta/{fqmn}")
    public HttpResponse<JsonObject> deleteMeta(@PathVariable String fqmn, @RequestBody JsonObject requestBody) {
        MetaplusDoc doc = new MetaplusDoc(requestBody);
        if (null != fqmn && !fqmn.isEmpty()) {
            if (!doc.getFqmn().equals(fqmn)) {
                throw new IllegalArgumentException("Fqmn '" + fqmn + "' in path is NOT equal to fqmn '" +
                        doc.getFqmn() + "' in doc.");
            }
        }
        patchService.deleteMeta(doc);
        return HttpResponse.ok();
    }

    @PostMapping("/update_plus/{fqmn}")
    public HttpResponse<JsonObject> updatePlus(@PathVariable String fqmn, @RequestBody JsonObject requestBody) {
        MetaplusDoc doc = new MetaplusDoc(requestBody);
        if (null != fqmn && !fqmn.isEmpty()) {
            if (!doc.getFqmn().equals(fqmn)) {
                throw new IllegalArgumentException("Fqmn '" + fqmn + "' in path is NOT equal to fqmn '" +
                        doc.getFqmn() + "' in doc.");
            }
        }
        patchService.updatePlus(doc);
        return HttpResponse.ok();
    }

}
