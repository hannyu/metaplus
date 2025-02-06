package com.outofstack.metaplus.common.model;


import com.outofstack.metaplus.common.json.JsonObject;

import java.util.regex.Pattern;


/**
 * A document template, which has common core fields.
 * Yet a JsonObject.
 *
 */
public abstract class MetaplusTemplate extends JsonObject {
    public final static String KEY_FQMN = "fqmn";
    public final static String KEY_FQMN_CORP = "corp";
    public final static String KEY_FQMN_DOMAIN = "domain";
    public final static String KEY_FQMN_NAME = "name";

    public final static String KEY_CTS = "cts";
    public final static String KEY_CTS_CREATED_BY = "createdBy";
    public final static String KEY_CTS_CREATED_AT = "createdAt";
    public final static String KEY_CTS_UPDATED_BY = "updatedBy";
    public final static String KEY_CTS_UPDATED_AT = "updatedAt";
    public final static String KEY_CTS_VERSION = "version";

    public static final String REGEX_FQMN_DOMAIN = "^[a-z][a-z0-9_.+\\-]{1,100}";
    public static final Pattern PATTERN_FQMN_DOMAIN = Pattern.compile(REGEX_FQMN_DOMAIN);
    public static final String REGEX_FQMN_NAME = "^[a-zA-Z0-9][a-zA-Z0-9_.+\\-]{1,400}";
    public static final Pattern PATTERN_FQMN_NAME = Pattern.compile(REGEX_FQMN_NAME);


    public MetaplusTemplate(JsonObject target) {
        super(target);

        JsonObject fqmnjo = target.getJsonObject(KEY_FQMN);
        if (null == fqmnjo) {
            throw new IllegalArgumentException("Fqmn can not be null");
        }
        String corp = fqmnjo.getString(KEY_FQMN_CORP);
        String domain = fqmnjo.getString(KEY_FQMN_DOMAIN);
        String name = fqmnjo.getString(KEY_FQMN_NAME);
        setFqmn(corp, domain, name);

        if (null == target.getJsonObject(KEY_CTS)) {
            setCts(new JsonObject());
        }
    }

    public MetaplusTemplate(String fqmn) {
        super();
        String[] ss = DocUtil.checkAndSplitFqmn(fqmn);
        setFqmn(ss[0], ss[1], ss[2]);
        setCts(new JsonObject());
    }

    public MetaplusTemplate(String corp, String domain, String name) {
        super();
        setFqmn(corp, domain, name);
        setCts(new JsonObject());
    }

//    /**
//     * check and cast JsonObject as MetaplusTemplate
//     *
//     * @param jsonObject
//     * @return  a MetaplusTemplate or throw IllegalArgumentException
//     */
//    public static MetaplusTemplate copyOf(JsonObject jsonObject) {
//        if (null == jsonObject) {
//            throw new IllegalArgumentException("jsonObject can not be null");
//        }
//        JsonObject fqmn = jsonObject.getJsonObject(KEY_FQMN);
//        if (null == fqmn || !fqmn.containsKey(KEY_FQMN_DOMAIN) || !fqmn.containsKey(KEY_FQMN_NAME)) {
//            throw new IllegalArgumentException("MetaTemplate must have valid FQMN");
//        }
//
//        if (null == jsonObject.getJsonObject(KEY_CTS)) {
//            jsonObject.put(KEY_CTS, new JsonObject());
//        }
//        merge(jsonObject);
//    }

    public void setFqmn(String corp, String domain, String name) {
        if (null == domain || domain.isEmpty()) {
            throw new IllegalArgumentException("Fqmn.domain can not be empty");
        }
        if (!PATTERN_FQMN_DOMAIN.matcher(domain).find()) {
            throw new IllegalArgumentException("Fqmn.domain '" + domain + "' does not match regex '" +
                    REGEX_FQMN_DOMAIN + "'");
        }

        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException("Fqmn.name can not be empty");
        }
        if (!PATTERN_FQMN_NAME.matcher(name).find()) {
            throw new IllegalArgumentException("Fqmn.name '" + name + "' does not match regex '" +
                    REGEX_FQMN_NAME + "'");
        }

        JsonObject fqmnjo = getJsonObject(KEY_FQMN);
        if (null == fqmnjo) {
            fqmnjo = new JsonObject();
            put(KEY_FQMN, fqmnjo);
        }
        fqmnjo.put(KEY_FQMN_CORP, corp);
        fqmnjo.put(KEY_FQMN_DOMAIN, domain);
        fqmnjo.put(KEY_FQMN_NAME, name);
    }

    public void setFqmn(String fqmn) {
        String[] ss = DocUtil.checkAndSplitFqmn(fqmn);
        setFqmn(ss[0], ss[1], ss[2]);
    }

    public String getFqmnCorp() {
        return getJsonObject(KEY_FQMN).getString(KEY_FQMN_CORP);
    }

    public String getFqmnDomain() {
        return getJsonObject(KEY_FQMN).getString(KEY_FQMN_DOMAIN);
    }

    public String getFqmnName() {
        return getJsonObject(KEY_FQMN).getString(KEY_FQMN_NAME);
    }

    public String getFqmn() {
        JsonObject jo = getJsonObject(KEY_FQMN);
        return jo.getString(KEY_FQMN_CORP) + "::" + jo.getString(KEY_FQMN_DOMAIN) + "::" +
                jo.getString(KEY_FQMN_NAME);
    }

    public JsonObject getCts() {
        return getJsonObject(KEY_CTS);
    }
    public void setCts(JsonObject cts) {
        put(KEY_CTS, cts);
    }
    public String getCtsCreatedBy() {
        return getJsonObject(KEY_CTS).getString(KEY_CTS_CREATED_BY);
    }
    public void setCtsCreatedBy(String createdBy) {
        getJsonObject(KEY_CTS).put(KEY_CTS_CREATED_BY, createdBy);
    }
    public void deleteCtsCreatedBy() {
        getJsonObject(KEY_CTS).remove(KEY_CTS_CREATED_BY);
    }
    public String getCtsCreatedAt() {
        return getJsonObject(KEY_CTS).getString(KEY_CTS_CREATED_AT);
    }
    public void setCtsCreatedAt(String createdAt) {
        getJsonObject(KEY_CTS).put(KEY_CTS_CREATED_AT, createdAt);
    }
    public void deleteCtsCreatedAt() {
        getJsonObject(KEY_CTS).remove(KEY_CTS_CREATED_AT);
    }
    public String getCtsUpdatedBy() {
        return getJsonObject(KEY_CTS).getString(KEY_CTS_UPDATED_BY);
    }
    public void setCtsUpdatedBy(String updatedBy) {
        getJsonObject(KEY_CTS).put(KEY_CTS_UPDATED_BY, updatedBy);
    }
    public void deleteCtsUpdatedBy() {
        getJsonObject(KEY_CTS).remove(KEY_CTS_UPDATED_BY);
    }
    public String getCtsUpdatedAt() {
        return getJsonObject(KEY_CTS).getString(KEY_CTS_UPDATED_AT);
    }
    public void setCtsUpdatedAt(String updatedAt) {
        getJsonObject(KEY_CTS).put(KEY_CTS_UPDATED_AT, updatedAt);
    }
    public void deleteCtsUpdatedAt() {
        getJsonObject(KEY_CTS).remove(KEY_CTS_UPDATED_AT);
    }
    public String getCtsVersion() {
        return getJsonObject(KEY_CTS).getString(KEY_CTS_VERSION);
    }
    public void setCtsVersion(int version) {
        getJsonObject(KEY_CTS).put(KEY_CTS_VERSION, version);
    }

}
