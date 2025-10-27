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


package com.xclite.api.config;

import com.jfinal.kit.PropKit;
import lombok.Setter;

/**
 * 缓存配置
 *
 * @author zhixiulee
 */
@Setter
public class Cache {
        /**
     * 缓存配置前缀常量
     */
    private static final String CONFIG_PREFIX = "lite-api.cache.";

    /**
     * 是否启用缓存
     */
    private boolean enable = false;

    /**
     * 默认缓存容量
     */
    private int capacity = 10000;

    /**
     * 默认过期时间,单位为毫秒，-1为不过期
     */
    private long ttl = -1;

    public int getCapacity() {
        return PropKit.getInt(CONFIG_PREFIX + "capacity", capacity);
    }

    public long getTtl() {
        return PropKit.getLong(CONFIG_PREFIX + "ttl", ttl);
    }

    public boolean isEnable() {
        return PropKit.getBoolean(CONFIG_PREFIX + "enable", enable);
    }

}
