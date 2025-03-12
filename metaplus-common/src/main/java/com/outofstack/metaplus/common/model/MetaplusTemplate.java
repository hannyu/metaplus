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

    public final static String KEY_SYNC = "sync";
    public final static String KEY_SYNC_CREATED_BY = "createdBy";
    public final static String KEY_SYNC_CREATED_AT = "createdAt";
    public final static String KEY_SYNC_CREATED_FROM = "createdFrom";
    public final static String KEY_SYNC_UPDATED_BY = "updatedBy";
    public final static String KEY_SYNC_UPDATED_AT = "updatedAt";
    public final static String KEY_SYNC_UPDATED_FROM = "updatedFrom";
    public final static String KEY_SYNC_VERSION = "version";

    public static final String REGEX_FQMN_DOMAIN = "^[a-z][a-z0-9_.+\\-]{1,100}";
    public static final Pattern PATTERN_FQMN_DOMAIN = Pattern.compile(REGEX_FQMN_DOMAIN);
    public static final String REGEX_FQMN_NAME = "^[a-zA-Z0-9][a-zA-Z0-9_.+()\\-\\[\\]]{1,400}";
    public static final Pattern PATTERN_FQMN_NAME = Pattern.compile(REGEX_FQMN_NAME);

    public MetaplusTemplate(String fqmn) {
        super();
        String[] ss = DocUtil.checkAndSplitFqmn(fqmn);
        setFqmn(ss[0], ss[1], ss[2]);
        setSync(new JsonObject());
    }

    public MetaplusTemplate(String corp, String domain, String name) {
        super();
        setFqmn(corp, domain, name);
        setSync(new JsonObject());
    }

    public MetaplusTemplate(JsonObject target) {
        super(target);
        checkAndLoad();
    }

    private void checkAndLoad() {
        JsonObject fqmnJo = getJsonObject(KEY_FQMN);
        if (null == fqmnJo) {
            throw new IllegalArgumentException("Fqmn can not be null");
        }
        String corp = fqmnJo.getString(KEY_FQMN_CORP);
        String domain = fqmnJo.getString(KEY_FQMN_DOMAIN);
        String name = fqmnJo.getString(KEY_FQMN_NAME);
        setFqmn(corp, domain, name);

        if (null == getJsonObject(KEY_SYNC)) {
            setSync(new JsonObject());
        }
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
//        if (null == jsonObject.getJsonObject(KEY_SYNC)) {
//            jsonObject.put(KEY_SYNC, new JsonObject());
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

    public JsonObject getSync() {
        return getJsonObject(KEY_SYNC);
    }
    public void setSync(JsonObject sync) {
        put(KEY_SYNC, sync);
    }

    public String getSyncCreatedBy() {
        return getJsonObject(KEY_SYNC).getString(KEY_SYNC_CREATED_BY);
    }
    public void setSyncCreatedBy(String createdBy) {
        getJsonObject(KEY_SYNC).put(KEY_SYNC_CREATED_BY, createdBy);
    }
    public void deleteSyncCreatedBy() {
        getJsonObject(KEY_SYNC).remove(KEY_SYNC_CREATED_BY);
    }

    public String getSyncCreatedAt() {
        return getJsonObject(KEY_SYNC).getString(KEY_SYNC_CREATED_AT);
    }
    public void setSyncCreatedAt(String createdAt) {
        getJsonObject(KEY_SYNC).put(KEY_SYNC_CREATED_AT, createdAt);
    }
    public void deleteSyncCreatedAt() {
        getJsonObject(KEY_SYNC).remove(KEY_SYNC_CREATED_AT);
    }

    public String getSyncCreatedFrom() {
        return getJsonObject(KEY_SYNC).getString(KEY_SYNC_CREATED_FROM);
    }
    public void setSyncCreatedFrom(String createdFrom) {
        getJsonObject(KEY_SYNC).put(KEY_SYNC_CREATED_FROM, createdFrom);
    }
    public void deleteSyncCreatedFrom() {
        getJsonObject(KEY_SYNC).remove(KEY_SYNC_CREATED_FROM);
    }

    public String getSyncUpdatedBy() {
        return getJsonObject(KEY_SYNC).getString(KEY_SYNC_UPDATED_BY);
    }
    public void setSyncUpdatedBy(String updatedBy) {
        getJsonObject(KEY_SYNC).put(KEY_SYNC_UPDATED_BY, updatedBy);
    }
    public void deleteSyncUpdatedBy() {
        getJsonObject(KEY_SYNC).remove(KEY_SYNC_UPDATED_BY);
    }

    public String getSyncUpdatedAt() {
        return getJsonObject(KEY_SYNC).getString(KEY_SYNC_UPDATED_AT);
    }
    public void setSyncUpdatedAt(String updatedAt) {
        getJsonObject(KEY_SYNC).put(KEY_SYNC_UPDATED_AT, updatedAt);
    }
    public void deleteSyncUpdatedAt() {
        getJsonObject(KEY_SYNC).remove(KEY_SYNC_UPDATED_AT);
    }

    public String getSyncUpdatedFrom() {
        return getJsonObject(KEY_SYNC).getString(KEY_SYNC_UPDATED_FROM);
    }
    public void setSyncUpdatedFrom(String updatedFrom) {
        getJsonObject(KEY_SYNC).put(KEY_SYNC_UPDATED_FROM, updatedFrom);
    }
    public void deleteSyncUpdatedFrom() {
        getJsonObject(KEY_SYNC).remove(KEY_SYNC_UPDATED_FROM);
    }

    public String getSyncVersion() {
        return getJsonObject(KEY_SYNC).getString(KEY_SYNC_VERSION);
    }
    public void setSyncVersion(int version) {
        getJsonObject(KEY_SYNC).put(KEY_SYNC_VERSION, version);
    }

}
