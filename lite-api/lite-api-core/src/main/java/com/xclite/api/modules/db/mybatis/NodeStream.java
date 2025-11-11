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
