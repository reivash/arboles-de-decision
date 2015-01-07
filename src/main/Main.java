package main;

import ide3.IDE3;
import tree.DecisionTree;
import data.DataManager;
import data.TableManager;
import draw.DecisionTreeDrawer;

/**
 * Test of the algorithm.
 * 
 * @author: Sanjay Belani Prem
 * @author: Jorge García González
 * @author: Francisco Nurudín Álvarez González
 * @author: Javier Cabero Guerra
 */
public class Main {

	public static void main(String[] args) {
		TableManager tm;
		DecisionTree tree;
		String fileName="example.csv";
		
		if (args.length > 0)
			fileName = args[0];
		
		System.out.println("Loading data from file " + fileName);
		tm = new DataManager(fileName);
		
		System.out.println("Applying ide3.");
		tree = IDE3.ide3(tm);
		
		System.out.println("Decision tree made successfully.");
		DecisionTreeDrawer.drawDecisionTree(tree);
	}
}
