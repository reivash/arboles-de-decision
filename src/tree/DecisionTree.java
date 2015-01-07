package tree;

import java.util.List;

import data.TableManager;

public interface DecisionTree {
	
	public void setAttribute(String att);
	
	public String getAttribute();
	
	public String evaluate(List<String> attributes, List<String> instance);
	
	public List<String> evaluate(TableManager instances);
	
	public boolean evaluate(TableManager instances, String file);
	
	public TableManager ClassifyImportedCases(String pathin, String pathout, String separator);

	public int getDepth();
	
	public boolean isLeaf();
	
	public int getMaxBranchingFactor();
}
