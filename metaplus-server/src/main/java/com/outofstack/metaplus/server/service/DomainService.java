package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.DomainDoc;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.schema.Properties;
import com.outofstack.metaplus.common.model.schema.Schema;
import com.outofstack.metaplus.server.MetaplusException;
import com.outofstack.metaplus.server.domain.DomainLib;

import com.outofstack.metaplus.server.domain.SchemaTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DomainService extends AbstractService{

    @Autowired
    private MetaService metaService;

    public void createDomain(DomainDoc domainDoc) {
        // 1 validate param
        validateDomainDoc(domainDoc);
        String name = domainDoc.getFqmnName();
        if (domainLib.existDomain(name)) {
            throw new MetaplusException("Domain '" + name + "' already exist");
        }

        // 2 validate privilege
        validatePrivilege();

        // 3. build
        String indexName = DomainLib.PREFIX_INDEX + domainDoc.getFqmnName();
        domainDoc.putByPath("$.meta.index.name", indexName);
        validateAndFixupByRules(domainDoc);
        SchemaTuple schemaTuple = domainLib.generateSchemaTuple(domainDoc);

        // 4. create index
        Boolean isAbstract = domainDoc.getBooleanByPath("$.meta.index.abstract");
        if (null == isAbstract || !isAbstract) {
            indexDao.createIndex(indexName, schemaTuple.getPureSchema());
            domainDoc.putByPath("$.meta.index.created", true);
        }

        // 5. create doc
        domainDoc.setSchema(schemaTuple.getRichSchema());
        metaService.create(domainDoc);
        domainLib.putDomainDoc(name, domainDoc);
    }

    public void updateDomain(DomainDoc domainDoc) {
        // 1 validate param
        validateDomainDoc(domainDoc);
        String name = domainDoc.getFqmnName();
        if (!domainLib.existDomain(name)) {
            throw new MetaplusException("Domain '" + name + "' is not exist");
        }

        // 2 validate privilege
        validatePrivilege();

        // 3. build
        DomainDoc oldDoc = domainLib.getDomainDoc(name);
        oldDoc.merge(domainDoc);
        String indexName = oldDoc.getStringByPath("$.meta.index.name");
        Boolean isAbstract = oldDoc.getBooleanByPath("$.meta.index.abstract");

        // 4. index
        if (null == isAbstract || !isAbstract) {
            Schema pureSchema = domainLib.rich2PureSchema(oldDoc.getSchema());
            Properties mappings = pureSchema.getMappings();
            if (!mappings.isEmpty()) {
                indexDao.updateMappings(indexName, mappings);
            }
            JsonObject settings = pureSchema.getSettings();
            if (!settings.isEmpty()) {
                indexDao.updateSettings(indexName, settings);
            }
        }

        // 4. doc
        metaService.update(oldDoc);
        domainLib.putDomainDoc(name, oldDoc);
    }

    public void deleteDomain(DomainDoc domainDoc) {
        // 1. check
        String domain = domainDoc.getFqmnName();
        if (DomainLib.isSystemDomain(domain)) {
            throw new MetaplusException("System domain '" + domain + "' can not be delete");
        }

        // 2. index
        domainDoc = domainLib.getDomainDoc(domain);
        String indexName = domainDoc.getStringByPath("$.meta.index.name");
        Boolean isAbstract = domainDoc.getBooleanByPath("$.meta.index.abstract");
        if (null == isAbstract || !isAbstract) {
            indexDao.deleteIndex(indexName);
        }

        // 3. doc
        metaService.delete(domainDoc);
        domainLib.removeDomain(domain);
    }

    public DomainDoc readDomain(String domain) {
        return domainLib.getDomainDoc(domain);
    }

    public boolean existDomain(String domain) {
        return domainLib.existDomain(domain);
    }

    public Set<String> listDomains() {
        return domainLib.listDomains();
    }

    public MetaplusDoc sample(String domain) {
        return domainLib.generateSampleDoc(domain);
    }

    /// //////////
    ///

    private void validateDomainDoc(DomainDoc domainDoc) {
        if (null == domainDoc) {
            throw new IllegalArgumentException("Doc is null");
        }

        String domain = domainDoc.getFqmnDomain();
        if (!DomainLib.DOMAIN_DOMAIN.equals(domain)) {
            throw new IllegalArgumentException("DomainDoc's domain must be 'domain', but '" + domain + "'");
        }

        Properties mappings = domainDoc.getSchema().getMappings();
        Properties settings = domainDoc.getSchema().getMappings();
        if (mappings.isEmpty() && settings.isEmpty()) {
            throw new IllegalArgumentException("DomainDoc must not have empty 'mappings' or 'settings'");
        }
    }



}
