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


package com.xclite.api.modules.db.provider;

/**
 * 列名转换接口
 *
 * @author zhixiulee
 */
public interface ColumnMapperProvider {

	/**
	 * 获取转换器名称
	 *
	 * @return 转换器名称
	 */
	String name();

	/**
	 * 转换方法
	 *
	 * @param columnName 列名
	 * @return 转换后的方法
	 */
	String mapping(String columnName);

	/**
	 * 反向转换
	 *
	 * @param name 转换后的值
	 * @return 列名
	 */
	default String unmapping(String name) {
		return name;
	}

}
