package com.outofstack.metaplus.common;


/**
 * Firstly, get from VM options
 * Secondly, get from Environment variable
 * Thirdly, get defaultValue
 * Finally, get null
 *
 * <code>
 * export metaplus_syncer_dir=/tmp/metaplus1
 * java -Dmetaplus_syncer_dir=/tmp/metaplus2 -jar app.jar arg1 arg2
 * </code>
 * PropertyUtil.getString("metaplus_syncer_dir") // => /tmp/metaplus2
 *
 */
public class PropertyUtil {

    public static String getString(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (null == value) {
            value = System.getenv(key);
            if (null == value) {
                value = defaultValue;
            }
        }
        return value;
    }

}
