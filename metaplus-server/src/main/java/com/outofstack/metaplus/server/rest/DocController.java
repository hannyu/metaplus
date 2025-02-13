package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.server.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/doc")
@RestController
public class DocController {

    @Autowired
    private DocService docService;

    @GetMapping("/read/{fqmn}")
    public HttpResponse<MetaplusDoc> read(@PathVariable String fqmn) {
        MetaplusDoc doc = docService.readDoc(fqmn);
        return new HttpResponse<MetaplusDoc>(200, doc);
    }


}
