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


package com.xclite.api.modules.db.mybatis;


import cn.hutool.core.util.StrUtil;
import com.xclite.api.utils.ScriptManager;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 对应XML中 <foreach>
 *
 * @author zhixiulee
 * @version : 2020-05-18
 */
@Setter
public class ForeachSqlNode extends SqlNode {
	/**
	 * 数据集合，支持Collection、数组
	 */
	private String collection;
	/**
	 * item 变量名
	 */
	private String item;
	/**
	 * 拼接起始SQL
	 */
	private String open;
	/**
	 * 拼接结束SQL
	 */
	private String close;
	/**
	 * 分隔符
	 */
	private String separator;

	/**
	 * 序号
	 */
	private String index;

    @Override
	public String getSql(Map<String, Object> paramMap, List<Object> parameters) {
		// 提取集合
		Object value = ScriptManager.executeExpression(this.collection, paramMap);
		// 如果集合为空，则过滤该节点
		if (value == null) {
			return "";
		}
		// 如果集合是Collection对象或其子类，则转成数组
		if (value instanceof Collection) {
			value = ((Collection<?>) value).toArray();
		}
		// 判断不是数组，则过滤子节点并返回
		if (!value.getClass().isArray()) {
			return "";
		}
		// 开始拼接SQL,
		StringBuilder sqlBuilder = new StringBuilder(StrUtil.nullToEmpty(this.open));
		boolean hasIndex = index != null && !index.isEmpty();
		// 获取数组长度
		int len = Array.getLength(value);
		for (int i = 0; i < len; i++) {
			// 存入item对象
			paramMap.put(this.item, Array.get(value, i));
			if (hasIndex) {
				paramMap.put(this.index, i);
			}
			// 拼接子节点
			sqlBuilder.append(executeChildren(paramMap, parameters));
			// 拼接分隔符
			if (i + 1 < len) {
				sqlBuilder.append(StrUtil.nullToEmpty(this.separator));
			}
		}
		// 拼接结束SQL
		sqlBuilder.append(StrUtil.nullToEmpty(this.close));
		return sqlBuilder.toString();
	}
}
