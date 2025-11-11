package com.xclite.api.config;

import com.jfinal.kit.PropKit;

public class RedisConfig {

    private static final String CONFIG_PREFIX = "lite-api.redis.";

    public static String getCacheName() {
        return PropKit.get(CONFIG_PREFIX + "cache-name", "main");
    }

    public static String getHost() {
        return PropKit.get(CONFIG_PREFIX + "host", "127.0.0.1");
    }

    public static int getPort() {
        return PropKit.getInt(CONFIG_PREFIX + "port", 6379);
    }

    public static int database() {
        return PropKit.getInt(CONFIG_PREFIX + "database", 0);
    }

    public static String getPassword() {
        return PropKit.get(CONFIG_PREFIX + "password");
    }
}
