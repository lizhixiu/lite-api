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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class NodeStream {

	private final List<Node> nodes;

	private int index = 0;

	private final int len;

	public NodeStream(NodeList nodeList) {
		this.nodes = filterCommentAndBlankNodes(nodeList);
		this.len = this.nodes.size();
	}

	private static List<Node> filterCommentAndBlankNodes(NodeList nodeList) {
		List<Node> nodes = new ArrayList<>();
		for (int i = 0, len = nodeList.getLength(); i < len; i++) {
			Node node = nodeList.item(i);
			short nodeType = node.getNodeType();
			if (nodeType != Node.COMMENT_NODE && (nodeType != Node.TEXT_NODE || !node.getNodeValue().trim().isEmpty())) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	public boolean match(String nodeName) {
		return hasMore() && nodeName.equalsIgnoreCase(this.nodes.get(this.index).getNodeName());
	}


	public boolean match(short nodeType) {
		return hasMore() && nodeType == this.nodes.get(this.index).getNodeType();
	}

	public Node consume() {
		return this.nodes.get(this.index++);
	}

	public boolean hasMore() {
		return this.index < this.len;
	}
}
