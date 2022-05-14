package com.eduardo.shortest.tree;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private List<Node> children = null;
	private char value;

	public Node(char value) {
		super();
		children = new ArrayList<>();
		this.value = value;
	}

	public void addChild(Node child) {
		children.add(child);
	}

	public List<Node> getChildren() {
		return children;
	}

	public char getValue() {
		return value;
	}

}
