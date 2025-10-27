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

import java.util.List;
import java.util.Map;

/**
 * 对应XML中 <where>
 *
 * @author zhangxu
 * @version : 2020-12-05
 */
public class WhereSqlNode extends TrimSqlNode {
	public WhereSqlNode() {
		this.prefix = "WHERE";
		this.prefixOverrides = "AND | OR | AND\n| OR\n| AND\r| OR\r| AND\t| OR\t";
	}

	@Override
	public String getSql(Map<String, Object> paramMap, List<Object> parameters) {
		String sql = super.getSql(paramMap, parameters);
		if (this.prefix.equals(sql.trim())) {
			return "";
		}
		return sql;
	}

}
