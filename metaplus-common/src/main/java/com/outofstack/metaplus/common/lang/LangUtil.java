package com.outofstack.metaplus.common.lang;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LangUtil {

    /**
     * diffOrderedList(left, right) ==> [onlyInLeft, onlyInRight]
     *
     * @param leftOrderedList
     * @param rightOrderedList
     * @param comparator
     * @return
     * @param <T>
     */
    public static <T> Pair<List<T>, List<T>> diffOrderedList(List<T> leftOrderedList, List<T> rightOrderedList,
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

        return new Pair<>(onlyInLeft, onlyInRight);
    }
}
