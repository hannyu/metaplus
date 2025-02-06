package com.outofstack.metaplus.common;

import java.util.List;
import java.util.StringJoiner;

public class StringUtil {

    public static String joinString(List<String> stringList, String delimiter) {
        StringJoiner sj = new StringJoiner(delimiter);
        for (String str : stringList) {
            sj.add(str);
        }
        return sj.toString();
    }

}
