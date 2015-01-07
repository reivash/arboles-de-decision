package tree;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import data.TableManager;

public class Leaf implements DecisionTree {

	String attribute;
	String value;

	// constructor
	public Leaf(String attribute, String value) {
		this.attribute = attribute;
		this.value = value;
	}

	//getters and setters for the class attributes
	public String getAttribute() {
		return this.attribute;
	}

	public void setAttribute(String att) {
		this.attribute = att;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String val) {
		this.value = val;
	}
	
	/**
	 * Evaluate recursively the objective value.
	 */
	public String evaluate(List<String> attributes, List<String> instances) {
		return value;
	}

	public List<String> evaluate(TableManager instances) {
		throw new RuntimeException("Error. Evaluate multiply instances in a Leaf.");
	}

	public int getDepth() {
		return 0;
	}

	public boolean isLeaf() {
		return true;
	}
	
	public int getMaxBranchingFactor() {
		return 1;
	}

	public TableManager ClassifyImportedCases(String pathin, String pathout, String separator) {
		throw new RuntimeException("Error. Imported cases in a leaf.");
	}

	public boolean evaluate(TableManager instances, String file) {
		boolean b=true;
		try{
			PrintWriter pw = new PrintWriter(file);
			String str="Out";
			for(String straux : instances.getAttributes()){
				str=str+","+straux;
			}
			pw.println(str);
			for(int i=0; i<instances.getColumn(0).size(); i++){
				str=value;
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
}
