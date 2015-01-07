package draw;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import tree.DecisionTree;
import tree.Leaf;
import tree.Node;

public class DrawableDecisionTree {

	public DrawableNode root = null;
	
	private static int defaultNodeWidth = 100;
	private static boolean surprise_me = false;	
	private static float   surprise_factor = 20;
	
	private ArrayList<ClassifyingExample> examples = new ArrayList<ClassifyingExample>();
	
	public DrawableDecisionTree(DecisionTree dt) {
		if (dt == null) {
			System.out.println("WARNING: Creating empty DrawableDecisionTree");
			return;
		}

		// Create tree copy
		if (dt.isLeaf())
			root = new DrawableLeaf(dt.getAttribute(), ((Leaf) dt).getValue());
		else {
			root = new DrawableNode(dt.getAttribute());
			root.x = DecisionTreeDrawer.SCREEN_WIDTH/2;
			root.y = 50;
			processTree(root, (Node) dt, 1);
		}
		
		orderTree();
	}
	
	public void classify(ArrayList<String> example) {
		examples.add(new ClassifyingExample(this, example));
	}

	private void orderTree() {
		ArrayList<DrawableLeaf> leafs = getLeafs(root);
		int step = DecisionTreeDrawer.SCREEN_WIDTH / leafs.size();
		int offsetX = step>>1;
		
		// Set leafs x
		for(DrawableLeaf dl : leafs) {
			dl.x = offsetX;
			offsetX += step;
		}
		
		HashSet<DrawableNode> fathers = new HashSet<DrawableNode>();
		for(DrawableLeaf dl : leafs)
			fathers.add(dl.parent);
		
		adjustFathers(fathers);
	}
	
	private void adjustFathers(HashSet<DrawableNode> fathers) {
		// Adjust fathers
		for(DrawableNode dn : fathers) {
			float x = 0;
			for(Tuple<String, DrawableNode> t : dn.children)
				x += t.right.x;
			x /= dn.children.size();
			dn.x = x;
		}
		
		// Get more fathers!
		HashSet<DrawableNode> moreFathers = new HashSet<DrawableNode>();
		for(DrawableNode dn : fathers) 
			if(dn.parent != null)
				moreFathers.add(dn.parent);
		
		if(!moreFathers.isEmpty())
			adjustFathers(moreFathers);
		else
			fixSizes(root);
	}

	private void fixSizes(DrawableNode dn) {
		for(Tuple<String, DrawableNode> t : dn.children) {
			t.right.relativePositions.put(dn, new Tuple<Float, Float>(dn.x - t.right.x, dn.y - t.right.y));
			dn.relativePositions.put(t.right, new Tuple<Float, Float>(t.right.x - dn.x, t.right.y - dn.y));
			fixSizes(t.right);
		}
	}

	private ArrayList<DrawableLeaf> getLeafs(DrawableNode node) {
		ArrayList<DrawableLeaf> leafs = new ArrayList<DrawableLeaf>();
		if(node instanceof DrawableLeaf)
			leafs.add((DrawableLeaf) node);
		else 
			for(Tuple<String, DrawableNode> t : node.children) 
				leafs.addAll(getLeafs(t.right));
		return leafs;
	}

	private void processTree(DrawableNode parent, Node parentDT, int depth) {
		int bfactor = parentDT.getMaxBranchingFactor();
		int maxWidth = defaultNodeWidth * bfactor;
		int step = maxWidth / parentDT.getChildren().size();
		int offsetX = -maxWidth/2 + defaultNodeWidth/2;
		for (String key : parentDT.children.keySet()) {
			DecisionTree n = parentDT.children.get(key);
			if (n.isLeaf()) {
				DrawableLeaf dl = new DrawableLeaf(n.getAttribute(), ((Leaf) n).getValue());
				dl.x = offsetX + parent.x;
				dl.y = dl.levelSpacing + parent.y;
				parent.addChildren(key, dl);
				dl.setParent(parent);
			} else {
				DrawableNode dn = new DrawableNode(n.getAttribute());
				dn.x = offsetX + parent.x;
				dn.y = dn.levelSpacing + parent.y;
				parent.addChildren(key, dn);
				dn.setParent(parent);
				processTree(dn, (Node) n, depth++);
			}
			offsetX += step;
		}
	}

	public void update() {
		for(ClassifyingExample e : examples)  
			e.update();
		
		if (root != null) {
			root.update();
			
			if(surprise_me) {
				Random r = new Random();
				root.x += r.nextGaussian()*surprise_factor;
				root.y += r.nextGaussian()*surprise_factor;
				root.adjust();
			}
		}
	}

	public void render() {
		for(ClassifyingExample e : examples) 
			e.render();
		if (root != null)
			root.render();
	}
}
