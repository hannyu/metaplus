package com.outofstack.metaplus.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public class LangUtil {

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

    /**
     *
     *
     */
    public static <T> List<List<T>> diffOrderedList(List<T> leftOrderedList, List<T> rightOrderedList,
                                             Comparator<T> comparator) {
        int i = 0, j = 0;
        List<T> onlyInLeft = new ArrayList<>();
        List<T> onlyInRight = new ArrayList<>();

        while (i < leftOrderedList.size() && j < rightOrderedList.size()) {
            int cmp = comparator.compare(leftOrderedList.get(i), rightOrderedList.get(j));
            if (cmp < 0) {
                onlyInLeft.add(leftOrderedList.get(i++));
            } else if (cmp > 0) {
                onlyInRight.add(rightOrderedList.get(j++));
            } else {
                i++;
                j++;
            }
        }

        while (i < leftOrderedList.size()) onlyInLeft.add(leftOrderedList.get(i++));
        while (j < rightOrderedList.size()) onlyInRight.add(rightOrderedList.get(j++));

        List<List<T>> res = new ArrayList<>();
        res.add(onlyInLeft);
        res.add(onlyInRight);
        return res;
    }
}
