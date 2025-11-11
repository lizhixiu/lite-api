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

import com.xclite.api.utils.ScriptManager;
import org.ssssssss.script.parsing.ast.literal.BooleanLiteral;

import java.util.List;
import java.util.Map;

/**
 * 对应XML中 <if>、<elseif>
 *
 * @author zhixiulee
 * @version : 2020-05-18
 */
public class IfSqlNode extends SqlNode {
	/**
	 * 判断表达式
	 */
	private final String test;

	private final SqlNode nextNode;

	public IfSqlNode(String test, SqlNode nextNode) {
		this.test = test;
		this.nextNode = nextNode;
	}

	@Override
	public String getSql(Map<String, Object> paramMap, List<Object> parameters) {
		// 执行表达式
		Object value = ScriptManager.executeExpression(test, paramMap);
		// 判断表达式返回结果是否是true，如果不是则过滤子节点
		if (BooleanLiteral.isTrue(value)) {
			return executeChildren(paramMap, parameters);
		}
		if (nextNode != null) {
			return nextNode.getSql(paramMap, parameters);
		}
		return "";
	}
}
