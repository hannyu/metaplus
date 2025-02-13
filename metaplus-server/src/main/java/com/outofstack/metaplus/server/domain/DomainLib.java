package com.outofstack.metaplus.server.domain;

import com.outofstack.metaplus.common.DateUtil;
import com.outofstack.metaplus.common.json.JsonArray;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.json.JsonRule;
import com.outofstack.metaplus.common.model.DomainDoc;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.schema.Field;
import com.outofstack.metaplus.common.model.schema.Schema;
import com.outofstack.metaplus.common.model.schema.Properties;
import com.outofstack.metaplus.server.MetaplusException;
import com.outofstack.metaplus.server.storage.EsClient;
import com.outofstack.metaplus.server.storage.EsResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DomainLib {
    public static final String PREFIX_INDEX =  "i_metaplus_of_";
    public static final String CORP_METAPLUS =  "metaplus";

    public static final String DOMAIN_NONE =  "none";
    public static final String DOMAIN_DOMAIN =  "domain";
    public static final String DOMAIN_TRASH =  "trash";
    public static final String DOMAIN_CHANGELOG =  "changelog";

    @Autowired
    private EsClient esClient;

    private final Map<String, DomainDoc> domainCache = new ConcurrentHashMap<>();;

    @PostConstruct
    private void init() {
        // FIXME: 100 move to searchDao
        String url = "/%s%s/_search?version=true&size=100".formatted(PREFIX_INDEX, DOMAIN_DOMAIN);
        EsResponse response = esClient.get(url);
        if (! response.isSuccess()) {
            throw new MetaplusException("initDomainCache fail, response code:" +
                    response.getStatusCode() + " and body:" + response.getBody());
        }
        JsonArray hits = response.getBody().getJsonObject("hits").getJsonArray("hits");

        for (int i=0; i<hits.size(); i++) {
            DomainDoc domainDoc = new DomainDoc(hits.getJsonObject(i).getJsonObject("_source"));
            String name = domainDoc.getFqmnName();
            domainCache.put(name, domainDoc);
        }
    }

    public DomainDoc getDomainDoc(String domain) {
//        initDomainCache();
        return domainCache.get(domain);
    }

    public void putDomainDoc(String domain, DomainDoc domainDoc) {
//        initDomainCache();
        domainCache.put(domain, domainDoc);
        rulesCache.remove(domain);
    }

    public boolean existDomain(String domain) {
//        initDomainCache();
        return DOMAIN_DOMAIN.equals(domain) || domainCache.containsKey(domain);
    }

    public void removeDomain(String domain) {
//        initDomainCache();
        domainCache.remove(domain);
        rulesCache.remove(domain);
    }

    public String getIndexName(String domain) {
        if (DOMAIN_DOMAIN.equals(domain)) {
            return PREFIX_INDEX + DOMAIN_DOMAIN;
        }

//        initDomainCache();
        DomainDoc domaindoc = domainCache.get(domain);
        if (null == domaindoc) {
            return null;
        }
        return domaindoc.getStringByPath("$.meta.index.name");
    }

    public String getAndCheckIndexName(String domain) {
        String indexName = getIndexName(domain);
        if (null == indexName) {
            throw new MetaplusException("Can not find index for domain '" + domain + "'");
        }
        return indexName;
    }

    public Map<String, DomainDoc> getAllDomainDocs() {
//        initDomainCache();
        return domainCache;
    }

    public Set<String> listDomains() {
//        initDomainCache();
        return domainCache.keySet();
    }

    public List<String> listCustomDomains() {
//        initDomainCache();
        List<String> customDomains = new ArrayList<>();
        Set<String> domains = domainCache.keySet();
        for (String domain : domains) {
            if (!isSystemDomain(domain)) {
                customDomains.add(domain);
            }
        }
        return customDomains;
    }

    public static boolean isSystemDomain(String domain) {
        return DOMAIN_NONE.equals(domain) || DOMAIN_DOMAIN.equals(domain) ||
                DOMAIN_TRASH.equals(domain) || DOMAIN_CHANGELOG.equals(domain);
    }

    /// ///////////////////////////////////////////
    /// Schema here
    /// ///////////////////////////////////////////

    private final Map<String, List<JsonRule>> rulesCache = new ConcurrentHashMap<>();

//    private volatile Map<String, SchemaTuple> schemaTuples = new ConcurrentHashMap<>();

//    private void initSchemaTuple(String domain) {
//        if (!existDomain(domain)) {
//            throw new MetaplusException("Domain '" + domain + "' is not exist");
//        } else if (!schemaTuples.containsKey(domain)) {
//            buildAndPutSchemaTupleRecursively(domain, getDomainDoc(domain));
//        }
//    }

//    public Map<String, SchemaTuple> getAllSchemaTuples() {
//        return schemaTuples;
//    }

//    public Schema getRichSchema(String domain) {
//        initSchemaTuple(domain);
//        return schemaTuples.get(domain).richSchema;
//    }

    public List<JsonRule> getRules(String domain) {
        if (rulesCache.containsKey(domain)) {
            return rulesCache.get(domain);
        } else if (existDomain(domain)){
            DomainDoc domainDoc = getDomainDoc(domain);
            List<JsonRule> rules = buildJsonRules(domainDoc.getSchema().getMappings());
            rulesCache.put(domain, rules);
            return  rules;
        } else {
            throw new MetaplusException("Domain '" + domain + "' does not exist");
        }
    }

//    public Schema getPureSchema(String domain) {
//        initSchemaTuple(domain);
//        return schemaTuples.get(domain).pureSchema;
//    }

    public MetaplusDoc generateSampleDoc(String domain) {
        if (existDomain(domain)) {
            MetaplusDoc doc = new MetaplusDoc(null, domain, "sample_" + new Random().nextInt(1000));
            Properties mappings = getDomainDoc(domain).getSchema().getMappings();
            buildSampleRecursively(doc, mappings);
            return doc;
        } else {
            throw new MetaplusException("Domain '" + domain + "' does not exist");
        }
    }

    /// ////////////////////////
    /// Recursively here
    /// ////////////////////////

//    /**
//     * Recursively find and merge schema.
//     *
//     * @param domainDoc
//     * @return schema
//     */
//    private void buildAndPutSchemaTupleRecursively(String domain, DomainDoc domainDoc) {
//        Stack<Schema> schemaStack = new Stack<>();
//        schemaStack.push(domainDoc.getSchema());
//        String superDomain = domainDoc.getStringByPath("$.meta.index.extend");
//        while (null != superDomain && !superDomain.isEmpty()) {
//            if (schemaTuples.containsKey(superDomain)) {
//                schemaStack.push(schemaTuples.get(superDomain).richSchema);
//                break;
//            } else if (existDomain(superDomain)){
//                DomainDoc superDomainDoc = getDomainDoc(superDomain);
//                buildAndPutSchemaTupleRecursively(superDomain, superDomainDoc);
//                schemaStack.push(schemaTuples.get(superDomain).richSchema);
//            } else {
//                throw new MetaplusException("Super domain '" + superDomain + "' does not exist");
//            }
//        }
//
//        Schema richSchema = new Schema();
//        while (!schemaStack.empty()) {
//            richSchema.copyMerge(schemaStack.pop());
//        }
//        Schema pureSchema = rich2PureSchema(richSchema);
//        List<JsonRule> rules = buildJsonRules(richSchema.getMappings());
//        schemaTuples.put(domain, new SchemaTuple(domainDoc.getSchema(), richSchema, pureSchema, rules));
//    }

    private Schema buildRichSchemaFromDomainDocRecursively(DomainDoc domainDoc) {
        Schema richSchema = new Schema();
        String superDomain = domainDoc.getStringByPath("$.meta.index.extend");
        if (null != superDomain && !superDomain.isEmpty()) {
            if (existDomain(superDomain)) {
                DomainDoc superDomainDoc = getDomainDoc(superDomain);
                Schema superSchema = buildRichSchemaFromDomainDocRecursively(superDomainDoc);
                richSchema.copyMerge(superSchema);
                richSchema.copyMerge(domainDoc.getSchema());
            } else {
                throw new MetaplusException("Super domain '" + superDomain + "' does not exist");
            }
        } else {
            richSchema.copyMerge(domainDoc.getSchema());
        }
        return richSchema;
    }

    public SchemaTuple generateSchemaTuple(DomainDoc domainDoc) {
        Schema richSchema = buildRichSchemaFromDomainDocRecursively(domainDoc);
        Schema pureSchema = rich2PureSchema(richSchema);
//        List<JsonRule> rules = buildJsonRules(richSchema.getMappings());
        return new SchemaTuple(domainDoc.getSchema(), richSchema, pureSchema);
    }

    public Schema rich2PureSchema(Schema richSchema) {
        Schema pureSchema = new Schema();
        if (null != richSchema.getMappings()) {
            Properties pureMappings = rich2PurePropertiesRecursively(richSchema.getMappings());
            pureSchema.setMappings(pureMappings);
        }
        if (null != richSchema.getSettings()) {
            pureSchema.setSettings(richSchema.getSettings());
        }
        return pureSchema;
    }

    public Properties rich2PurePropertiesRecursively(Properties richProperties) {
        if (null == richProperties) return null;

        Properties pureProperties = new Properties();
        for (String key : richProperties.propertyKeySet()) {
            JsonObject property = richProperties.getProperty(key);
            if (Properties.isProperties(property)) {
                Properties childProperties = rich2PurePropertiesRecursively(new Properties(property));
                pureProperties.putProperty(key, childProperties);
            } else if (Field.isField(property)) {
                pureProperties.putProperty(key, new Field(property).toPureCopy());
            } else {
                throw new IllegalArgumentException("Invalid property '" + property + "' with key '" + key + "'");
            }
        }
        return pureProperties;
    }

    public List<JsonRule> buildJsonRules(Properties mappings) {
        List<JsonRule> jsonRules = new ArrayList<>();
        String jsonPath = "$";
        buildJsonRulesRecursively(jsonRules, jsonPath, mappings);
        return jsonRules;
    }

    private void buildJsonRulesRecursively(List<JsonRule> rules, String jsonPath, Properties properties) {
        for (String key : properties.propertyKeySet()) {
            JsonObject property = properties.getProperty(key);
            if (Properties.isProperties(property)) {
                buildJsonRulesRecursively(rules, jsonPath + "." + key, new Properties(property));
            } else if (Field.isField(property)) {
                Field field = new Field(property);
                String sComment = field.getComment();
                Boolean bRequired = field.getRequired();
                Object oDefault = field.getDefault();
                if ( (null != sComment && !sComment.isEmpty()) || (null != bRequired && !bRequired) ||
                        (null != oDefault)) {
                    rules.add(new JsonRule(jsonPath + "." + key, sComment, bRequired, oDefault));
                }
            } else {
                throw new IllegalArgumentException("Invalid property '" + property + "' with key '" + key + "'");
            }
        }
    }


    private void buildSampleRecursively(JsonObject sample, Properties properties) {
        if (null == properties) return;
        for (String key : properties.propertyKeySet()) {
            JsonObject property = properties.getProperty(key);
            if (Properties.isProperties(property)) {
                JsonObject child = sample.getJsonObject(key);
                if (null == child) {
                    child = new JsonObject();
                    sample.put(key, child);
                }
                buildSampleRecursively(child, new Properties(property));
            } else if (Field.isField(property)) {
                if (null == sample.get(key)) {
                    Field field = new Field(property);
                    String type = field.getType();
                    Object oDefault = field.getDefault();
                    boolean required = null != field.getRequired() && field.getRequired();
                    if (null != oDefault) {
                        sample.put(key, oDefault);
                    } else {
                        if ("keyword".equals(type)) {
                            if (required) {
                                sample.put(key, "*keyword");
                            } else {
                                sample.put(key, "");
                            }
                        } else if ("text".equals(type)) {
                            if (required) {
                                sample.put(key, "*text");
                            } else {
                                sample.put(key, "");
                            }
                        } else if ("object".equals(type)) {
                            sample.put(key, new JsonObject());
                        } else if ("boolean".equals(type)) {
                            sample.put(key, false);
                        } else if ("date".equals(type)) {
                            sample.put(key, DateUtil.formatNow());
                        } else if ("long".equals(type) || "integer".equals(type) || "short".equals(type) ||
                                "byte".equals(type) || "double".equals(type) || "float".equals(type)) {
                            sample.put(key, 0);
                        } else if ("version".equals(type)) {
                            sample.put(key, "1.0.0");
                        } else if ("alias".equals(type)) {
                            // nothing
                        } else {
                            // TODO: binary? alias? completion? dense_vector? ...
                            sample.put(key, "?? unknown type '" + type + "'");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Invalid property '" + property + "' with key '" + key + "'");
            }
        }
    }
}
