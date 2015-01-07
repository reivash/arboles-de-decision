package tree;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.DataManager;
import data.TableManager;

public class Node implements DecisionTree {

	//Name of the attribute
	String attribute;

	//Map of the children corresponding to the current node
	public Map<String, DecisionTree> children;

	public Node(String attribute, Map<String, DecisionTree> children) {
		this.attribute = attribute;
		this.children = children;
	}
	
	public Node(String attribute){
		this.attribute = attribute;
		children = new HashMap<String, DecisionTree>();
	}
	
	//Setters and getters for the attributes
	public String getAttribute(){
		return this.attribute;
	}
	
	public void setAttribute(String att){
		this.attribute=att;
	}
	
	public void setChildren(Map<String, DecisionTree> children){
		this.children = children;
	}
	
	public Collection<DecisionTree> getChildren(){
		return children.values();
	}
	
	public String toString(){
		return "[" + attribute + ", " + children.toString() + "]";
	}
	
	//Adds a key and a node linked with that key.
	public void addChild(String value, DecisionTree t){
		children.put(value, t);
	}

	/**
	 * Evaluate recursively the objective value.
	 */
	public String evaluate(List<String> attributes, List<String> instance) {
		DecisionTree subTree = null;
		int attColumn = attributes.lastIndexOf(attribute);
		if(attColumn == -1) throw new RuntimeException("Error with the evaluation. Attribute "+attribute+" not found. "+attributes);
		subTree = children.get(instance.get(attColumn));
		//System.out.println("Debug: children: "+children+" attribute: "+attribute+" map: "+map);
		return subTree.evaluate(attributes, instance);
	}

	public List<String> evaluate(TableManager instances) {
		List<String> list = new ArrayList<String>();
		for(int i=0; i<instances.getColumn(0).size(); i++){
			list.add(evaluate(instances.getAttributes(), instances.getRow(i)));
		}
		return list;
	}
	
	public boolean evaluate(TableManager instances, String file){
		boolean b=true;
		try{
			List<String> list = evaluate(instances);
			PrintWriter pw = new PrintWriter(file);
			String str="Out";
			for(String straux : instances.getAttributes()){
				str=str+","+straux;
			}
			pw.println(str);
			for(int i=0; i<instances.getColumn(0).size(); i++){
				str=list.get(i);
				for(String straux : instances.getRow(i)){
					str=str+","+straux;
				}
				pw.println(str);
			}
			pw.close();
		}catch(FileNotFoundException e){
			b=false;
		}
		return b;
	}
	
	
	public int getDepth() {
		int maxDepth = -1;
		for(DecisionTree dt : children.values()) {
			int depth = dt.getDepth();
			if(depth > maxDepth)
				maxDepth = depth;
		}
		return maxDepth + 1;
	}

	public boolean isLeaf() {
		return false;
	}
	
	public int getMaxBranchingFactor() {
		int max = 0;
		for(DecisionTree dt : children.values()){
			int bfactor = dt.getMaxBranchingFactor();
			if(bfactor > max)
				max = bfactor;
		}
		return max * children.values().size();
	}

	public TableManager ClassifyImportedCases(String pathin, String pathout, String separator) {
		TableManager tm = new DataManager(pathin);
		return tm;
	}
	
}
