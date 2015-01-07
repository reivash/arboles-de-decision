package draw;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

public class DrawableNode {
	
	public float x, y;
	public float w, h;
	public float borderX = 15f, 
				 borderY = 5f;
	
	public float speed = .1f;
	
	public float targetX, targetY;
	
	public float levelSpacing = 80f;
	public float childSpacing = 5f;
	
	private String attribute;
	public DrawableNode parent = null;
	public ArrayList<Tuple<String, DrawableNode>> children = new ArrayList<Tuple<String, DrawableNode>>();
	
	protected static TrueTypeFont font = new TrueTypeFont(Font.decode("Small Fonts Normal"), false);
	
	public HashMap<DrawableNode, Tuple<Float, Float>> relativePositions = new HashMap<DrawableNode, Tuple<Float, Float>>();
	public DrawableNode target = null;
	private float eps = 0.0001f;
	public boolean selected;
	private static boolean someNodeSelected;
	
	public DrawableNode(String attribute) {
		this.attribute = attribute;
		this.w = font.getWidth(attribute);
		this.h = font.getHeight();
	}
	
	public void addChildren(String value, DrawableNode dn) {
		this.children.add(new Tuple<String, DrawableNode>(value, dn));
		this.relativePositions.put(dn, new Tuple<Float, Float>(dn.x - x, dn.y - y));
	}
	
	public void setParent(DrawableNode parent) {
		this.parent = parent;
		this.relativePositions.put(parent, new Tuple<Float, Float>(parent.x - x, parent.y - y));
	}
	
	public float getChildDrawingXSize() {
		float size = childSpacing;
		for(Tuple<String, DrawableNode> t: children) 
			size += t.right.getChildDrawingXSize() + childSpacing;
		return size;
	}
	
	public void update() {
		if(selected) {
			
			x += MouseWrapper.getDX();
			y += MouseWrapper.getDY();
			
			if(MouseWrapper.wasReleased()) {
				selected = false;
				someNodeSelected = false;
			}
			
		} else {
			
			if(MouseWrapper.wasPressed() && mouseInBounds()) {
				selected = true;
				someNodeSelected = true;
				adjust();
			}
		}
		
		if(target != null) {
			// Adjust to target
			float relativeX = target.x - x;
			float relativeY = target.y - y;
			
			Tuple<Float, Float> relPos = relativePositions.get(target);
			float dx = relativeX - relPos.left;
			float dy = relativeY - relPos.right;
			
			x += dx * speed;
			y += dy * speed;
			
			if(Math.abs(dx) < eps && Math.abs(dy) < eps && !someNodeSelected) 
				target = null;
		}
		
		// Update children
		for(Tuple<String, DrawableNode> t: children) 
			t.right.update();
	}

	public void adjust() {
		for(Tuple<String, DrawableNode> t: children) 
			t.right.adjustToFather();
		if(parent != null) parent.adjustToChild(this);
	}

	public void adjustToChild(DrawableNode child) {
		target = child;
		for(Tuple<String, DrawableNode> t: children) 
			if(!t.right.equals(child))
				t.right.adjustToFather();
		if(parent != null) parent.adjustToChild(this);
	}
	
	public void adjustToFather() {
		target = parent;
		for(Tuple<String, DrawableNode> t: children) 
			t.right.adjustToFather();
	}
	
	private boolean mouseInBounds() {
		int mx = MouseWrapper.getX();
		int my = MouseWrapper.getY();
		return x-borderX <= mx && mx < x+w+borderX
				&& y-borderY <= my && my < y+h+borderY;
	}

	public void render() {
		drawLines();
		drawFrame();
		printAttribute();
		
		// Draw children
		for(Tuple<String, DrawableNode> t: children) 
			t.right.render();
	}

	private void drawLines() {
		// Draw lines to children
		for(Tuple<String, DrawableNode> t : children) { 
			DrawableNode dn = t.right;
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(getCenterX(), y);
			GL11.glVertex2f(dn.getCenterX(), dn.y);
			GL11.glEnd();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			font.drawString(dn.x + (x-dn.x)/2, dn.y + (y-dn.y)/2, t.left);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}

	protected void printAttribute() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		font.drawString(x, y, attribute);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	protected void drawFrame() {
		// Draw frame
		GL11.glColor3f(.4f, .4f, .4f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x-borderX,   y-borderY);
		GL11.glVertex2f(x-borderX,   y+h+borderY);
		GL11.glVertex2f(x+w+borderX, y+h+borderY);
		GL11.glVertex2f(x+w+borderX, y-borderY);
		GL11.glEnd();
	}
	
	public float getCenterX() {
		return x+font.getWidth(attribute)/2;
	}
}
