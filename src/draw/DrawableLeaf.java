package draw;

import org.lwjgl.opengl.GL11;


public class DrawableLeaf extends DrawableNode {

	public String value;
	
	public DrawableLeaf(String name, String value) {
		super(name);
		this.value = value!=null ? value : "NULL";
		this.w = font.getWidth(this.value);
		
	}
	
	public float getChildDrawingXSize() {
		float size = w + 2*borderX;
		return size;
	}
	
	public void render() {
		drawFrame();
		printValue();
	}
	
	protected void printValue() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		font.drawString(x, y, value);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	public float getCenterX() {
		return x+font.getWidth(value)/2;
	}
}
