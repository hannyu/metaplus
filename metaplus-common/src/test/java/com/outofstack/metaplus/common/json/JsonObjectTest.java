package com.outofstack.metaplus.common.json;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonObjectTest {

    @Test
    public void testOne() {
        String json1 = "{\"id\":123,\"height\":175.3,\"name\":\"han\",\"friends\":{\"jack\":\"good\",\"rose\":{\"age\":[18,20],\"sex\":false}},\"sex\":true}";
        JsonObject jo = JsonObject.parse(json1);
        assertEquals(json1, jo.toJson());
        assertEquals(123, jo.getInteger("id"));
        assertEquals("han", jo.getString("name"));
        assertEquals(123, jo.get("id"));
//        System.out.println("id:" + jo.get("id"));
        assertEquals(123.0d, jo.getDouble("id"));
        assertEquals(175.3f, jo.getFloat("height"));
        assertEquals(175L, jo.getLong("height"));
        assertEquals((short)175, jo.getShort("height"));
        assertEquals("175.3", jo.getString("height"));

        assertEquals("good", jo.getJsonObject("friends").getString("jack"));
        assertEquals(false, jo.getJsonObject("friends").getJsonObject("rose").getBoolean("sex"));
        assertEquals(20, jo.getJsonObject("friends").getJsonObject("rose").getJsonArray("age").getInteger(1));

        assertNull(jo.getString("noexist1"));
        assertNull(jo.getJsonObject("noexist2"));

        assertEquals(5, jo.size());
        assertFalse(jo.containsKey("noexist3"));
        assertFalse(jo.isEmpty());

    }

    @Test
    public void testTwo() {
        JsonObject jo = new JsonObject();
        assertEquals("{}", jo.toString());

        jo.put("name", "higgs");
        assertEquals("{\"name\":\"higgs\"}", jo.toString());

        jo.put("age", 18.8);
        jo.remove("name");
        assertEquals("{\"age\":18.8}", jo.toString());

        JsonObject jo2 = new JsonObject();
        jo2.put("copy", "me").put("yes?", true);
        jo2.putAll(jo);
        assertEquals("{\"copy\":\"me\",\"yes?\":true,\"age\":18.8}", jo2.toString());

        System.out.println(jo2.toJson());
    }

    @Test
    public void testThree() {
        JsonObject jo1 = new JsonObject();
        jo1.put("color", "red");

        JsonObject jo2 = JsonObject.parse("{\"color\":\"red\"}");

        JsonObject jo3 = new JsonObject();
        jo3.put("color", "red");

        assertEquals(jo1, jo2);
        assertEquals(jo1, jo3);
    }

    @Test
    public void testFour() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("number", 5);
        map1.put("duck", new JsonArray("gaga", "haha"));
        JsonObject jo1 = new JsonObject(map1);

        JsonObject jo2 = JsonObject.parse("{\"number\":5,\"duck\":[\"gaga\",\"haha\"]}");
        assertEquals(jo1, jo2);
        assertEquals(jo1.toJson(), jo2.toJson());

        assertEquals("[\"gaga\",\"haha\"]", jo1.getJsonArray("duck").toJson());

        System.out.println(jo1.getJsonArray("duck"));
        System.out.println(jo1);
    }

    @Test
    public void testFive() {
        JsonObject jo1 = JsonObject.parse("{\"number\":5,\"duck\":[\"gaga\",\"haha\"]}");
        System.out.println(jo1);
        assertEquals("{\"number\":5,\"duck\":[\"gaga\",\"haha\"]}", jo1.toJson());

        jo1 = JsonObject.parse("{\"numb😃er\":5,\"duck\":[\"gaga\",\"ha我的锅ha\"]}");
        System.out.println(jo1);
        assertEquals("{\"numb😃er\":5,\"duck\":[\"gaga\",\"ha我的锅ha\"]}", jo1.toJson());

        jo1 = JsonObject.parse("{\"number\":5,\"duck\":[\"gaga\",\"haha\"],\"45\":32}");
        System.out.println(jo1);
        assertNull(jo1.get("46"));

        assertThrows(JsonException.class, () -> {
            JsonObject jo = JsonObject.parse("{\"number\":5,\"duck\":[\"gaga\",\"haha\"],45:32}");
            jo.getDouble("duck");
        });

        assertThrows(JsonException.class, () -> {
            JsonObject.parse("{\"number\":5,\"duck\":[\"gaga,\"haha\"],45:32}");
        });
    }

    /**
     * Test merge, copyOf, deepMerge
     */
    @Test
    public void testSix() {
        JsonObject jo1 = JsonObject.parse("{\"num\":5,\"duck\":[\"gaga\",\"haha\"],\"attr\":{\"aa\":\"bb\",\"cc\":\"dd\",\"ee\":{\"ff\":\"gg\"}}}");
        JsonObject jo2 = JsonObject.parse("{\"num\":\"6\",\"yo\":77,\"duck\":[\"haha\"],\"attr\":{\"aa\":88,\"kk\":[1,2],\"ee\":{\"ff\":\"uu\"}}}");
        jo1.merge(jo2);
        assertEquals("{\"num\":\"6\",\"duck\":[\"haha\"],\"attr\":{\"aa\":88,\"cc\":\"dd\",\"ee\":{\"ff\":\"uu\"},\"kk\":[1,2]},\"yo\":77}", jo1.toJson());
//        System.out.println(jo1);

        JsonObject jo3 = JsonObject.parse("{\"num\":\"6\",\"duck\":[\"haha\"],\"attr\":{\"aa\":88,\"cc\":\"dd\",\"ee\":{\"ff\":\"uu\"},\"kk\":[1,2]},\"yo\":77}");
        assertEquals(jo3, jo1);

        JsonObject jo4 = JsonObject.parse("{\"num\":5,\"duck\":[\"gaga\",\"haha\"],\"attr\":{\"aa\":\"bb\",\"cc\":\"dd\"}}");
        JsonObject jo5 = jo4.deepCopy();
        jo4.getJsonObject("attr").put("aa", "jj");
        System.out.println(jo5);
        assertEquals("jj", jo4.getJsonObject("attr").getString("aa"));
        assertEquals("bb", jo5.getJsonObject("attr").getString("aa"));

        JsonObject jo6 = JsonObject.parse("{\"num\":5,\"duck\":[{\"j\":\"gaga\"}],\"x\":{\"y\":{\"z\":9}}}");
        JsonObject jo7 = JsonObject.parse("{\"duck\":[2,3],\"x\":{\"y\":{\"h\":10}}}");
        jo6.copyMerge(jo7);
        jo6.putByPath("$.x.y.h", 11);
        assertEquals("{\"num\":5,\"duck\":[2,3],\"x\":{\"y\":{\"z\":9,\"h\":11}}}", jo6.toJson());
        assertEquals(10, jo7.getByPath("$.x.y.h"));
        System.out.println(jo6);
        System.out.println(jo7);
    }

    /**
     * ByPath
     */
    @Test
    public void testSeven() {
        JsonObject jo1 = JsonObject.parse("{\"num\":5,\"duck\":[\"gaga\",\"dodo\"],\"attr\":{\"aa\":\"bb\"}," +
                "\"nested\":[{},{},{\"yes\":[{},{\"no\":5}]}]}");
        assertEquals("bb", jo1.getByPath("$.attr.aa"));
        jo1.putByPath("$.x.y.z", 555);
        assertEquals(555, jo1.getByPath("$.x.y.z"));

        assertThrows(JsonException.class, () -> {
            jo1.putByPath("$.duck.yes", "no");
        });

        jo1.putByPath("$.num", "6");
        assertEquals("6", jo1.getString("num"));
        System.out.println(jo1);

        // array in path
        assertEquals("dodo", jo1.getStringByPath("$.duck[1]"));

        jo1.putByPath("$.duck[2]", "xixi");
        System.out.println(jo1.toJson());

        assertEquals(5, jo1.getIntegerByPath("$.nested[2].yes[1].no"));

        assertThrows(Exception.class, () -> {
            jo1.getStringByPath("$.duck[5]");
        });

    }

    @Test
    public void testEight() {
        // Boolean
        JsonObject jo1 = JsonObject.parse("{\"isYes\":false,\"isNo\":true, \"ss\":1}");
        assertFalse(jo1.getBoolean("isYes"));
        assertTrue(jo1.getBoolean("isNo"));

//        // Jackson throws, but Fastjson2 do not
//        assertThrows(JsonException.class, () -> {
//            System.out.println("ss: " + jo1.getBoolean("ss"));
//        });
    }

    public static class MyObject extends JsonObject {}
    @Test
    public void testTen() {
        // generics
        JsonObject jo1 = JsonObject.parse("{\"num\":5,\"attr\":{\"aa\":\"bb\"}}");

        MyObject obj = jo1.getJsonObject("attr").cast(MyObject.class);
        System.out.println("MyObject: " + obj);
    }



}