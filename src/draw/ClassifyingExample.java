package draw;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class ClassifyingExample {
	
	private ArrayList<String> example;
	private boolean classifying = false;
	private float x, y, w=60, h=25, eps = 0.05f, speed = .1f;
	private DrawableNode target;
	private boolean ready_for_next_step = false;
	private int classify_interval = 35;
	private int nextStep = classify_interval;
	
	public ClassifyingExample(DrawableDecisionTree t, ArrayList<String> example) {
		this.classifying = true;
		this.example = example;
		this.target = t.root;
	}
	
	public void update() {
		if(classifying) {
			if(ready_for_next_step) {
				if(nextStep <= 0) {
					// Check next target
					boolean found = false;
					for(Tuple<String, DrawableNode> t : target.children) {
						if(t.left.equals(example.get(0))) {
							example.remove(0);
							target = t.right;
							found = true;
							break;
						}
					}
					if(!found) {
						if(!example.isEmpty())
							example.remove(0);
						else
							classifying = false;
						return;
					}
					nextStep = classify_interval;
					ready_for_next_step = false;
				} else {
					nextStep--;
				}
			} else {
				// Go to next node
				float dx = target.x - x;
				float dy = target.y - y;
				
				x += dx*speed;
				y += dy*speed;
				
				if(Math.abs(dx) < eps && Math.abs(dy) < eps) {
					ready_for_next_step = true;
					if(example.isEmpty()) {
						classifying = false;
						System.out.println("Finished classifying!");
					}
				}
			}
		} else {
			// Go to next node
			float dx = target.x - x;
			float dy = target.y - y;
			if(Math.abs(dx) >= eps || Math.abs(dy) >= eps) {
				x += dx*speed;
				y += dy*speed;
			}
		}
	}
	
	public void render() {
		GL11.glColor3f(.1f, 1f, .1f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x-w/2, y-h/2);
			GL11.glVertex2f(x-w/2, y+h/2);
			GL11.glVertex2f(x+w/2, y+h/2);
			GL11.glVertex2f(x+w/2, y-h/2);
		GL11.glEnd();
		GL11.glColor3f(1f, 1f, 1f);
	}
}
