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
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * sql节点
 *
 * @author zhixiulee
 * @version : 2020-05-18
 */
public abstract class SqlNode {

	/**
	 * 子节点
	 */
	List<SqlNode> nodes = new ArrayList<>();
	/**
	 * SQL参数
	 */
    @Getter
    List<Object> parameters;

	/**
	 * 追加子节点
	 */
	public void addChildNode(SqlNode node) {
		this.nodes.add(node);
	}

	/**
	 * 获取该节点的SQL
	 */
	public String getSql(Map<String, Object> paramMap) {
		this.parameters = new ArrayList<>();
		return getSql(paramMap, parameters);
	}

	/**
	 * 获取该节点的SQL
	 */
	public abstract String getSql(Map<String, Object> paramMap, List<Object> parameters);

	/**
	 * 获取子节点SQL
	 */
	public String executeChildren(Map<String, Object> paramMap, List<Object> parameters) {
		StringBuilder sqlBuilder = new StringBuilder();
		for (SqlNode node : nodes) {
			sqlBuilder.append(StrUtil.nullToEmpty(node.getSql(paramMap, parameters)));
			sqlBuilder.append(" ");
		}
		return sqlBuilder.toString();
	}

}
