package com.outofstack.metaplus.common.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonArrayTest {

    @Test
    public void testOne() {
        String json1 = "[12,34,[56,78],9,{\"a\":0}]";
        JsonArray ja = JsonArray.parse(json1);
        assertEquals(json1, ja.toJson());

        assertEquals(5, ja.size());
        assertEquals((short)12, ja.getShort(0));
        assertEquals(78f, ja.getJsonArray(2).getFloat(1));
        assertEquals(0, ja.getJsonObject(4).getFloat("a"));

        System.out.println(ja.toJson());
    }

    @Test
    public void testTwo() {
        JsonArray ja = new JsonArray();
        assertEquals("[]", ja.toString());

        ja.add("higgs");
        assertEquals("[\"higgs\"]", ja.toString());

        ja.add(18.8);
        ja.add(0, "jackson");
        ja.remove(2);
        assertEquals("[\"jackson\",\"higgs\"]", ja.toString());

        JsonArray ja2 = new JsonArray();
        ja2.add("copy", "me").add("yes?");
        ja.addAll(ja2);
        assertEquals("[\"jackson\",\"higgs\",\"copy\",\"me\",\"yes?\"]", ja.toString());
        System.out.println(ja.toJson());
    }

    @Test
    public void testThree() {
        JsonArray ja1 = new JsonArray();
        ja1.add("color", "red");

        JsonArray ja2 = JsonArray.parse("[\"color\",\"red\"]");

        JsonArray ja3 = new JsonArray();
        ja3.add("color", "red");

        assertEquals(ja1, ja2);
        assertEquals(ja1, ja3);
    }

    @Test
    public void testFour() {
//        JsonArray ja1 = new JsonArray("gaga", "haha", new String[]{"jiji", "kaka"});
        JsonArray ja1 = new JsonArray("gaga", "haha", new JsonArray("jiji", "kaka"));

        JsonArray ja2 = JsonArray.parse("[\"gaga\",\"haha\",[\"jiji\",\"kaka\"]]");
        assertEquals(ja1, ja2);
        assertEquals(ja1.toJson(), ja2.toJson());

        assertEquals("[\"jiji\",\"kaka\"]", ja1.getJsonArray(2).toJson());

        System.out.println(ja1.getJsonArray(2));
        System.out.println(ja1);
    }

    @Test
    public void testFive() {
        JsonArray ja1 = JsonArray.parse("[\"number\",5,\"duck\",{\"yell\":\"haha\",\"777\":888}]");
        System.out.println(ja1);
        assertEquals("[\"number\",5,\"duck\",{\"yell\":\"haha\",\"777\":888}]", ja1.toJson());

        ja1 = JsonArray.parse("[\"numb😃er\",5,\"duck\",[\"gaga\",\"ha我的锅ha\"]]");
        System.out.println(ja1);
        assertEquals("[\"numb😃er\",5,\"duck\",[\"gaga\",\"ha我的锅ha\"]]", ja1.toJson());

        ja1 = JsonArray.parse("[\"number\",5,null,[\"gaga\",\"haha\"],45,32]");
        System.out.println(ja1);
        assertNull(ja1.getObject(2));

        assertThrows(JsonException.class, () -> {
            JsonArray ja = JsonArray.parse("[\"number,5,null,[\"gaga\",\"haha\"],45,32]");
        });

        assertThrows(JsonException.class, () -> {
            JsonArray ja = JsonArray.parse("[\"number\",5,null,[\"gaga\",\"haha\"],45,32]");
            ja.getLong(0);
        });
    }

    @Test
    public void testSix() {
        JsonArray a1 = JsonArray.parse("[2,3,4]");
        Object[] arr = a1.toArray();
        System.out.println("arr: " + arr);
        assertEquals(3, arr.length);
        assertEquals(3, arr[1]);
    }
}