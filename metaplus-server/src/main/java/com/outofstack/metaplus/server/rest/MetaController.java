package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.server.service.MetaService;
import com.outofstack.metaplus.server.service.PatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/meta")
@RestController
public class MetaController {

    @Autowired
    private MetaService metaService;

    @PutMapping("/create/{fqmn}")
    public HttpResponse<JsonObject> create(@PathVariable String fqmn, @RequestBody JsonObject requestBody) {
        MetaplusDoc doc = new MetaplusDoc(requestBody);
        if (null != fqmn && !fqmn.isEmpty()) {
            if (!doc.getFqmn().equals(fqmn)) {
                throw new IllegalArgumentException("Fqmn '" + fqmn + "' in path is NOT equal to fqmn '" +
                        doc.getFqmn() + "' in doc.");
            }
        }
        metaService.create(doc);
        return HttpResponse.ok();
    }

    @PostMapping("/update/{fqmn}")
    public HttpResponse<JsonObject> update(@PathVariable String fqmn, @RequestBody JsonObject requestBody) {
        MetaplusDoc doc = new MetaplusDoc(requestBody);
        if (null != fqmn && !fqmn.isEmpty()) {
            if (!doc.getFqmn().equals(fqmn)) {
                throw new IllegalArgumentException("Fqmn '" + fqmn + "' in path is NOT equal to fqmn '" +
                        doc.getFqmn() + "' in doc.");
            }
        }
        metaService.update(doc);
        return HttpResponse.ok();
    }

    @DeleteMapping("/delete/{fqmn}")
    public HttpResponse<JsonObject> delete(@PathVariable String fqmn, @RequestBody JsonObject requestBody) {
        MetaplusDoc doc = new MetaplusDoc(requestBody);
        if (null != fqmn && !fqmn.isEmpty()) {
            if (!doc.getFqmn().equals(fqmn)) {
                throw new IllegalArgumentException("Fqmn '" + fqmn + "' in path is NOT equal to fqmn '" +
                        doc.getFqmn() + "' in doc.");
            }
        }
        metaService.delete(doc);
        return HttpResponse.ok();
    }

    @RequestMapping("/exist/{fqmn}")
    public ResponseEntity<HttpResponse<JsonObject>> exist(@PathVariable String fqmn) {
        if (metaService.exist(fqmn)) {
            return ResponseEntity.ok().body(HttpResponse.ok());
        } else {
            return ResponseEntity.status(404).body(HttpResponse.notFound());
        }
    }

}
