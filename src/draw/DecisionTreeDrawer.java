package draw;


import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import tree.DecisionTree;
import tree.Leaf;
import tree.Node;

public class DecisionTreeDrawer {

	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	private static DrawableDecisionTree ddt;
	private static int example_interval = 1500;
	private static int wait_time = example_interval;
	
	public static void drawDecisionTree(DecisionTree dt) {
		setNativesPath();
		initOpenGL();
		initDisplay();
		
		ddt = new DrawableDecisionTree(dt);
		createRandomExamples(5);
		
		while(!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
			MouseWrapper.poll();
			ddt.update();
			ddt.render();
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}

	public static void createRandomExamples(int n) {
		for(int i = 0; i < n; i++) {
			new Thread() {
				public void run() {
					try {
						wait_time += example_interval; 
						Thread.sleep(wait_time);
						ArrayList<String> myList = new ArrayList<String>();
						DrawableNode current = ddt.root;
						Random r = new Random();
						while(true) {
							if(current.children.isEmpty())
								break;
							
							Tuple<String, DrawableNode> t = current.children.get(r.nextInt(current.children.size()));
							myList.add(t.left);
							current = t.right;
						}
						ddt.classify(myList);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	private static void setNativesPath() {
		System.setProperty("java.library.path", "lib");
		System.setProperty("org.lwjgl.librarypath", new java.io.File(
				"lib/natives").getAbsolutePath());
	}

	private static void initOpenGL() {
		try {
			Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
			Display.setTitle("Decision Tree Drawer");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	private static void initDisplay() {
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	/* Test Drawer */
	public static void main(String[] args) {
		
		System.out.println("*** DecisionTreeDrawer Test ***");
		
		System.out.println("Creating sample...");
		Node dt = new Node("Azúcar");
		
		Node sal = new Node("Sal");
		sal.addChild("Baja",  new Leaf("Pimienta", "Pellizco"));
		sal.addChild("Media", new Leaf("Pimienta", "Puñao"));
		sal.addChild("Alta",  new Leaf("Pimienta", "Barril"));
		
		Node curcuma = new Node("Curcuma");
		curcuma.addChild("Pechín", new Leaf("Pimienta", "Puñao"));
		curcuma.addChild("Ginebra", new Leaf("Pimienta", "Jartá"));
		
		dt.addChild("Poca", sal);
		dt.addChild("Chorrón", curcuma);
		
		System.out.println("Calling drawer...");
		drawDecisionTree(dt);
	}
}
