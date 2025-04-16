package com.outofstack.metaplus.common;

import com.outofstack.metaplus.common.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class LangUtilTest {

    @Test
    public void testOne() {
        List<String> list1 = new ArrayList<>();
        list1.add("aaa");
        list1.add("bbb");
        list1.add("ccc");

        List<String> list2 = new ArrayList<>();
        list2.add("bbb");
        list2.add("ccc");
        list2.add("ddd");
        list2.add("eee");

        List<List<String>> difs = LangUtil.diffOrderedList(list1, list2, String::compareTo);
        System.out.println("difs: " + JsonObject.object2JsonString(difs));
    }
}
