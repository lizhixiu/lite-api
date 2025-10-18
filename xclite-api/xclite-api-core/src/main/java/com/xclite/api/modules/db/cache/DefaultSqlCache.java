/*
 * Copyright 2025 xclite.com and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.xclite.api.modules.db.cache;

/**
 * 默认SQL缓存实现
 *
 * @author zhixiulee
 */
public class DefaultSqlCache implements SqlCache {

    private final LRUCache cache;

    public DefaultSqlCache(int capacity, long expire) {
        this.cache = new LRUCache(capacity, expire);
    }

    @Override
    public void put(String name, String key, Object value) {
        cache.put(name, key, value);
    }

    @Override
    public void put(String name, String key, Object value, long ttl) {
        cache.put(name, key, value, ttl);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object get(String name, String key) {
        return cache.get(name, key);
    }

    @Override
    public void delete(String name) {
        cache.delete(name);
    }


}
