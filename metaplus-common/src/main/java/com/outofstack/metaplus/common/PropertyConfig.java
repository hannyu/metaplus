package com.outofstack.metaplus.common;


import java.net.InetAddress;
import java.net.UnknownHostException;

/**

 *
 */
public class PropertyConfig {

    /**
     * Giving key = "a.b.c"
     * Firstly, get "a.b.c" from VM options
     * Secondly, trans "a.b.c" to "A_B_C", and get "A_B_C" from Environment variable
     * Thirdly, get defaultValue
     * Finally, get null
     *
     * <code>
     * export METAPLUS_SYNCER_DIR=/tmp/metaplus1
     * java -Dmetaplus.syncer.dir=/tmp/metaplus2 -jar app.jar arg1 arg2
     *
     * PropertyUtil.getString("metaplus.syncer.dir") // => /tmp/metaplus2
     * </code>
     */
    public static String get(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (null == value) {
            String newkey = key.toUpperCase().replaceAll("\\.", "_");
            value = System.getenv(newkey);
            if (null == value) {
                value = defaultValue;
            }
        }
        return value;
    }

    public static String get(String key) {
        return get(key, null);
    }

    /// //////////////////////
    /// build-in defined
    /// //////////////////////



    /// dir
    public static final String KEY_SYNCER_DIR = "metaplus.syncer.dir";

    private static String syncerDir = null;
    public static String getSyncerDir() {
        if (null == syncerDir) {
            String dir = PropertyConfig.get(KEY_SYNCER_DIR);
            if (null == dir) {
                dir = PropertyConfig.get("java.io.tmpdir", "");
            }
            syncerDir = dir;
        }
        return syncerDir;
    }

    /// fqmn.corp
    public static final String KEY_SYNCER_FQMN_CORP = "metaplus.syncer.fqmn.corp";
    public static final String DEFAULT_SYNCER_FQMN_CORP = "";

    private static String fqmnCorp = null;
    public static String getFqmnCorp() {
        if (null == fqmnCorp) {
            fqmnCorp = PropertyConfig.get(KEY_SYNCER_FQMN_CORP, DEFAULT_SYNCER_FQMN_CORP);
        }
        return fqmnCorp;
    }

    /// hostname
    public static final String KEY_SYNCER_HOSTNAME = "metaplus.syncer.hostname";

    private static String syncerHostname = null;
    public static String getSyncerHostname() {
        if (null == syncerHostname) {
            String hostname = PropertyConfig.get(KEY_SYNCER_HOSTNAME, "");
            if (hostname.isEmpty()) {
                try {
                    hostname = InetAddress.getLocalHost().getHostName();
                } catch (UnknownHostException ignored) {}
            }
            syncerHostname = hostname;
        }
        return syncerHostname;
    }



}
