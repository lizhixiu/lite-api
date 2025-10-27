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

import com.xclite.api.utils.MD5Utils;

import java.util.Arrays;

/**
 * SQL缓存接口
 *
 * @author zhixiulee
 */
public interface SqlCache {

	/**
	 * 计算key
	 */
	default String buildSqlCacheKey(String sql, Object[] params) {
		return MD5Utils.encrypt(sql + ":" + Arrays.toString(params));
	}


	/**
	 * 存入缓存
	 *
	 * @param name  名字
	 * @param key   key
	 * @param value 值
	 */
	void put(String name, String key, Object value);

	/**
	 * 存入缓存
	 *
	 * @param name  名字
	 * @param key   key
	 * @param value 值
	 * @param ttl   有效期
	 */
	void put(String name, String key, Object value, long ttl);

	/**
	 * 获取缓存
	 *
	 * @param name 名字
	 * @param key  key
	 */
	<T> T get(String name, String key);

	/**
	 * 删除缓存
	 *
	 * @param name 名字
	 */
	void delete(String name);

}
