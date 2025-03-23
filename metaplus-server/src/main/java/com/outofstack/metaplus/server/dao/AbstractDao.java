package com.outofstack.metaplus.server.dao;

import com.outofstack.metaplus.server.storage.EsClient;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao {

    @Autowired
    protected EsClient esClient;

    @Autowired
    protected DomainLib domainLib;

    /// ///////////
    ///



    public static final String SCRIPT_FUNC_MERGE =
            " void merge(def obj1, def obj2) { " +
                " if (obj1 == null || obj2 == null) { return; }" +
                " for (def entry : obj2?.entrySet()) { " +
                    " if (obj1[entry.getKey()] instanceof Map && entry.getValue() instanceof Map) { " +
                        " merge(obj1[entry.getKey()], entry.getValue()); " +
                    " } else { " +
                        " obj1[entry.getKey()] = entry.getValue(); " +
                    " } " +
                " } " +
            " } ";

    public static final String SCRIPT_FUNC_RENAME_FQMN =
            "void renameFqmn(def oldfqmn, def fqmn) {" +
            "    if (fqmn?.corp != null) {oldfqmn.corp = fqmn.corp;}" +
            "    if (fqmn?.domain != null) {oldfqmn.domain = fqmn.domain;}" +
            "    if (fqmn?.name != null) {oldfqmn.name = fqmn.name;}" +
            "    oldfqmn.fqmn = oldfqmn.corp + \"::\" + oldfqmn.domain + \"::\" + oldfqmn.name;" +
            "} ";

    public static final String SCRIPT_VAR_INIT =
            "def fqmn = ctx._source.fqmn; def sync = ctx._source.sync; " +
            "def meta = ctx._source.meta; def plus = ctx._source.plus; ";

    public static final String SCRIPT_MERGE_SYNC = "merge(sync, params.sync); merge(meta, params.meta);" +
            " merge(plus, params.plus);";

    public static final String SCRIPT_ALL_IN_ONE = SCRIPT_FUNC_MERGE + SCRIPT_FUNC_RENAME_FQMN +
            SCRIPT_VAR_INIT + SCRIPT_MERGE_SYNC;

}
