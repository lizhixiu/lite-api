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
