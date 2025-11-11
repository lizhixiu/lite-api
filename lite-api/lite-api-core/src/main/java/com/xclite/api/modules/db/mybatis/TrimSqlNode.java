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
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 对应XML中 <trim>，注意prefixOverrides和suffixOverrides大小写敏感
 *
 * @author zhangxu
 * @version : 2020-12-05
 */
public class TrimSqlNode extends SqlNode {
	/**
	 * 前缀  prefix
	 */
	@Setter
    protected String prefix;
	/**
	 * 后缀  suffix
	 */
	@Setter
    protected String suffix;
	/**
	 * 前缀 prefixOverrides
	 */
	protected String prefixOverrides;
	/**
	 * 后缀 suffixOverrides
	 */
	protected String suffixOverrides;

    public void setSuffixOverrides(String suffixOverrides) {
		this.suffixOverrides = suffixOverrides == null ? null : suffixOverrides.toUpperCase();
	}

	public void setPrefixOverrides(String prefixOverrides) {
		this.prefixOverrides = prefixOverrides == null ? null : prefixOverrides.toUpperCase();
	}

	@Override
	public String getSql(Map<String, Object> paramMap, List<Object> parameters) {
		StringBuilder sqlBuffer = new StringBuilder();
		String childrenSql = executeChildren(paramMap, parameters).trim();
		// 如果子节点不为null，则转成数组
		if (StrUtil.isNotEmpty(childrenSql)) {
			String upperSql = childrenSql.toUpperCase();
			// 开始拼接SQL,
			sqlBuffer.append(StrUtil.nullToEmpty(this.prefix)).append(" ");
			//去掉prefixOverrides
			if (StrUtil.isNotEmpty(this.prefixOverrides)) {
				String[] overrideArray = this.prefixOverrides.split("\\|");
				for (String override : overrideArray) {
					if (upperSql.startsWith(override)) {
						childrenSql = childrenSql.substring(upperSql.indexOf(override) + override.length()).trim();
						upperSql = childrenSql.toUpperCase();
						break;
					}
				}
			}
			//去掉suffixOverrides
			if (StrUtil.isNotBlank(this.suffixOverrides)) {
				String[] overrideArray = this.suffixOverrides.split("\\|");
				for (String override : overrideArray) {
					if (upperSql.endsWith(override)) {
						childrenSql = childrenSql.substring(0, upperSql.lastIndexOf(override));
						break;
					}
				}
			}
			sqlBuffer.append(childrenSql);
			// 拼接结束SQL
			sqlBuffer.append(" ").append(StrUtil.nullToEmpty(this.suffix));
		}
		return sqlBuffer.toString();
	}
}
