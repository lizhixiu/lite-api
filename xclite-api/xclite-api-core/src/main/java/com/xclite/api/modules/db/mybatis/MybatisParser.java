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

import com.xclite.api.exception.LiteApiException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.regex.Pattern;

public class MybatisParser {

	private static final Pattern ESCAPE_LT_PATTERN = Pattern.compile("<([\\d'\"\\s=>#$?(])");

	private static final String ESCAPE_LT_REPLACEMENT = "&lt;$1";

	public static SqlNode parse(String xml) {
		try {
			xml = "<lite-api>" + escapeXml(xml) + "</lite-api>";
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
			SqlNode sqlNode = new TextSqlNode("");
			parseNodeList(sqlNode, new NodeStream(document.getDocumentElement().getChildNodes()));
			return sqlNode;
		} catch (Exception e) {
			throw new LiteApiException("SQL解析错误", e);
		}
	}

	private static String escapeXml(String xml) {
		return ESCAPE_LT_PATTERN.matcher(xml).replaceAll(ESCAPE_LT_REPLACEMENT);
	}

	private static void parseNodeList(SqlNode sqlNode, NodeStream stream) {
		while (stream.hasMore()) {
			SqlNode childNode;
			if (stream.match(Node.TEXT_NODE)) {
				childNode = new TextSqlNode(stream.consume().getNodeValue().trim());
			} else {
				if (stream.match("foreach")) {
					childNode = parseForeachSqlNode(stream);
				} else if (stream.match("if")) {
					childNode = parseIfSqlNode(stream);
				} else if (stream.match("trim")) {
					childNode = parseTrimSqlNode(stream);
				} else if (stream.match("set")) {
					childNode = parseSetSqlNode(stream);
				} else if (stream.match("where")) {
					childNode = parseWhereSqlNode(stream);
				} else {
					throw new UnsupportedOperationException("Unsupported tags :" + stream.consume().getNodeName());
				}
			}
			sqlNode.addChildNode(childNode);
		}
	}

	private static IfSqlNode parseIfSqlNode(NodeStream stream) {
		Node ifNode = stream.consume();
		String test = getNodeAttributeValue(ifNode, "test");
		SqlNode nextNode = null;
		if (stream.match("else")) {
			nextNode = new TextSqlNode("");
			parseNodeList(nextNode, new NodeStream(stream.consume().getChildNodes()));
		} else if (stream.match("elseif")) {
			nextNode = parseIfSqlNode(stream);
		}
		return processChildren(new IfSqlNode(test, nextNode), ifNode);
	}

	private static <T extends SqlNode> T processChildren(T sqlNode, Node node) {
		if (node.hasChildNodes()) {
			parseNodeList(sqlNode, new NodeStream(node.getChildNodes()));
		}
		return sqlNode;
	}

	/**
	 * 解析foreach节点
	 */
	private static ForeachSqlNode parseForeachSqlNode(NodeStream stream) {
		Node node = stream.consume();
		ForeachSqlNode foreachSqlNode = new ForeachSqlNode();
		foreachSqlNode.setCollection(getNodeAttributeValue(node, "collection"));
		foreachSqlNode.setSeparator(getNodeAttributeValue(node, "separator", ","));
		foreachSqlNode.setClose(getNodeAttributeValue(node, "close", ")"));
		foreachSqlNode.setOpen(getNodeAttributeValue(node, "open", "("));
		foreachSqlNode.setItem(getNodeAttributeValue(node, "item"));
		foreachSqlNode.setIndex(getNodeAttributeValue(node, "index"));
		return processChildren(foreachSqlNode, node);
	}

	/**
	 * 解析trim节点
	 */
	private static TrimSqlNode parseTrimSqlNode(NodeStream stream) {
		Node node = stream.consume();
		TrimSqlNode trimSqlNode = new TrimSqlNode();
		trimSqlNode.setPrefix(getNodeAttributeValue(node, "prefix"));
		trimSqlNode.setPrefixOverrides(getNodeAttributeValue(node, "prefixOverrides"));
		trimSqlNode.setSuffix(getNodeAttributeValue(node, "suffix"));
		trimSqlNode.setSuffixOverrides(getNodeAttributeValue(node, "suffixOverrides"));
		return processChildren(trimSqlNode, node);
	}

	/**
	 * 解析set节点
	 */
	private static SetSqlNode parseSetSqlNode(NodeStream stream) {
		return processChildren(new SetSqlNode(), stream.consume());
	}

	/**
	 * 解析where节点
	 */
	private static WhereSqlNode parseWhereSqlNode(NodeStream stream) {
		return processChildren(new WhereSqlNode(), stream.consume());
	}

	private static String getNodeAttributeValue(Node node, String attributeKey, String defaultValue) {
		Node item = node.getAttributes().getNamedItem(attributeKey);
		return item != null ? item.getNodeValue() : defaultValue;
	}

	private static String getNodeAttributeValue(Node node, String attributeKey) {
		return getNodeAttributeValue(node, attributeKey, null);
	}
}
