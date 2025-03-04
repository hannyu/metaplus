package com.outofstack.metaplus.common.json;

//import org.springframework.util.ClassUtils;

import java.io.Reader;

public class JsonProxyFactory {

    private static boolean fastjson2Present;
    private static boolean jacksonPresent;
    static {
        ClassLoader loader = JsonProxyFactory.class.getClassLoader();

//        fastjson2Present = ClassUtils.isPresent("com.alibaba.fastjson2.JSON", loader);
//        jacksonPresent = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", loader);

        try {
            loader.loadClass("com.alibaba.fastjson2.JSON");
            fastjson2Present = true;
        } catch (Throwable e) {
            fastjson2Present = false;
        }

        try {
            loader.loadClass("com.fasterxml.jackson.databind.ObjectMapper");
            jacksonPresent = true;
        } catch (Throwable e) {
            jacksonPresent = false;
        }

    }

    public static JsonObjectProxy createJsonObjectProxy() {
        if (fastjson2Present) {
            return new JsonObjectProxy4Fastjson2();
        } else if (jacksonPresent) {
            return new JsonObjectProxy4Jackson();
        } else {
            throw new JsonException("Failed to find any JSON parsing library. Try to import Fastjson2 or Jackson.");
        }
    }

    public static JsonObjectProxy parseJsonObjectProxy(Reader reader) {
        if (fastjson2Present) {
            return JsonObjectProxy4Fastjson2.parse(reader);
        } else if (jacksonPresent) {
            return JsonObjectProxy4Jackson.parse(reader);
        } else {
            throw new JsonException("Failed to find any JSON parsing library. Try to import Fastjson2 or Jackson.");
        }
    }

    public static JsonArrayProxy createJsonArrayProxy() {
        if (fastjson2Present) {
            return new JsonArrayProxy4Fastjson2();
        } else if (jacksonPresent) {
            return new JsonArrayProxy4Jackson();
        } else {
            throw new JsonException("Failed to find any JSON parsing library. Try to import Fastjson2 or Jackson.");
        }
    }

    public static JsonArrayProxy parseJsonArrayProxy(Reader reader) {
        if (fastjson2Present) {
            return JsonArrayProxy4Fastjson2.parse(reader);
        } else if (jacksonPresent) {
            return JsonArrayProxy4Jackson.parse(reader);
        } else {
            throw new JsonException("Failed to find any JSON parsing library. Try to import Fastjson2 or Jackson.");
        }
    }

    public static String object2JsonString(Object object) {
        if (fastjson2Present) {
            return JsonObjectProxy4Fastjson2.object2JsonString(object);
        } else if (jacksonPresent) {
            return JsonObjectProxy4Jackson.object2JsonString(object);
        } else {
            throw new JsonException("Failed to find any JSON parsing library. Try to import Fastjson2 or Jackson.");
        }
    }


}
