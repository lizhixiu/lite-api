/*
 * MIT License
 *
 * Copyright (c) 2020 小东
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


package com.xclite.api.modules.db.cache;

/**
 * 默认SQL缓存实现
 *
 * @author mxd
 * 修改者: zhixiulee
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
