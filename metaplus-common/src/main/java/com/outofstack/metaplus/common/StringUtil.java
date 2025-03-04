package com.outofstack.metaplus.common;

import java.util.List;
import java.util.StringJoiner;

public class StringUtil {

    /**
     * joinString(["a", "b", "c"], ":") -> "a:b:c"
     *
     */
    public static String join(List<String> strList, String delimiter) {
        StringJoiner sj = new StringJoiner(delimiter);
        for (String str : strList) {
            sj.add(str);
        }
        return sj.toString();
    }

}
