package com.eduardo.shortest.main;

import com.eduardo.shortest.tree.SearchTree;

public class Main {

	public static void main(String[] args) {
		char[][] grid = { { '0', '*', '0', 's' }, { '*', '0', '*', '*' }, { '0', '*', '*', '*' },
				{ 'd', '*', '*', '*' } };
		
		TreeBuilder treeBuilder = new TreeBuilder();
		SearchTree st = treeBuilder.buildNewTree(grid);
		int output = st.minDistance();
		
		System.out.println(output);
	}

}
