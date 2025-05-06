package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonArray;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.DomainDoc;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.server.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/domain")
@RestController
public class DomainController {

    @Autowired
    private DomainService domainService;

    @PutMapping("/create/{domain}")
    public HttpResponse<JsonObject> create(@PathVariable String domain, @RequestBody JsonObject requestBody) {
        DomainDoc doc = new DomainDoc(requestBody);
        if (!doc.getFqmnName().equals(domain)) {
            throw new IllegalArgumentException("domain '" + domain + "' in url is NOT equal to domain '" +
                    doc.getFqmnName() + "' in doc.");
        }
        domainService.createDomain(doc);
        return HttpResponse.ok();
    }

    @PostMapping("/update/{domain}")
    public HttpResponse<JsonObject> update(@PathVariable String domain, @RequestBody JsonObject requestBody) {
        DomainDoc doc = new DomainDoc(requestBody);
        if (!doc.getFqmnName().equals(domain)) {
            throw new IllegalArgumentException("domain '" + domain + "' in url is NOT equal to domain '" +
                    doc.getFqmnName() + "' in doc.");
        }
        domainService.updateDomain(doc);
        return HttpResponse.ok();
    }

    @DeleteMapping("/delete/{domain}")
    public HttpResponse<JsonObject> delete(@PathVariable String domain, @RequestBody JsonObject requestBody) {
        DomainDoc doc = new DomainDoc(requestBody);
        if (!doc.getFqmnName().equals(domain)) {
            throw new IllegalArgumentException("domain '" + domain + "' in url is NOT equal to domain '" +
                    doc.getFqmnName() + "' in doc.");
        }
        domainService.deleteDomain(doc);
        return HttpResponse.ok();
    }

    @GetMapping("/read/{domain}")
    public HttpResponse<DomainDoc> read(@PathVariable String domain) {
        DomainDoc domainDoc = domainService.readDomain(domain);
        return HttpResponse.ok(domainDoc);
    }

    @RequestMapping("/exist/{domain}")
    public ResponseEntity<HttpResponse<JsonObject>> exist(@PathVariable String domain) {
        if (domainService.existDomain(domain)) {
            return ResponseEntity.ok().body(HttpResponse.ok());
        } else {
            return ResponseEntity.status(404).body(HttpResponse.notFound());
        }
    }

    @GetMapping("/sample/{domain}")
    public HttpResponse<MetaplusDoc> sample(@PathVariable String domain) {
        MetaplusDoc doc = domainService.sample(domain);
        return HttpResponse.ok(doc);
    }

    @GetMapping("/list")
    public HttpResponse<JsonObject> list() {
        Set<String> domains = domainService.listDomains();
        return HttpResponse.ok(new JsonObject("domains", new JsonArray().addAll(domains.toArray())));
    }

}
