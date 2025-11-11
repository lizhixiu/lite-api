package com.xclite.boot.admin.cache;

import java.util.HashMap;
import java.util.Map;

public class CodeCacheMap {

    private static final Map<String, String> map = new HashMap<>();

    public static void put(String key, String value) {
        map.put(key, value);
    }

    public static void remove(String key) {
        map.remove(key);
    }

    public static String get(String key) {
        return map.get(key);
    }

}
