package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.server.service.PatchService;
import com.outofstack.metaplus.server.service.PlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/plus")
@RestController
public class PlusController {

    @Autowired
    private PlusService plusService;

    @PostMapping("/update/{fqmn}")
    public HttpResponse<JsonObject> update(@PathVariable String fqmn, @RequestBody JsonObject requestBody) {
        MetaplusDoc doc = new MetaplusDoc(requestBody);
        if (null != fqmn && !fqmn.isEmpty()) {
            if (!doc.getFqmn().equals(fqmn)) {
                throw new IllegalArgumentException("Fqmn '" + fqmn + "' in path is NOT equal to fqmn '" +
                        doc.getFqmn() + "' in doc.");
            }
        }
        plusService.update(doc);
        return HttpResponse.ok();
    }

}
