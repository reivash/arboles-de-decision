package draw;

import org.lwjgl.input.Mouse;


public class MouseWrapper {
	private static class InputSingleton {
		private static final MouseWrapper INSTANCE = new MouseWrapper();
	}

	public static MouseWrapper getInstance() {
		return InputSingleton.INSTANCE;
	}

	private static boolean wasDownBefore;
	private static boolean pressed, released;
	private static int x, y, dx, dy, prex, prey;
	
	public static void poll() {
		if (Mouse.isButtonDown(0)) {
			if (!wasDownBefore) {
				pressed = true;
				wasDownBefore = true;
//				Log.info("Mouse pressed");
			} else {
				pressed = false;
			}
			released = false;
		} else {
			if (wasDownBefore) {
				released = true;
				wasDownBefore = false;
//				Log.info("Mouse released");
			} else {
				released = false;
			}
			pressed = false;
		}

		dx = Mouse.getDX();
		dy = Mouse.getDY();
		prex = x;
		prey = y;
		x = Mouse.getX();
		y = Mouse.getY();
	}

	public static boolean wasPressed() {
		return pressed;
	}

	public static boolean wasReleased() {
		return released;
	}

	public static int getX() {
		return x;
	}

	public static int getY() {
		return DecisionTreeDrawer.SCREEN_HEIGHT - y;
	}

	public static boolean isButtonDown(int idx) {
		return Mouse.isButtonDown(idx);
	}

	/* When the mouse is not grabbed it is more precise to calculate dx on your own */
	public static int getCalculatedDX() {
		return x - prex;
	}
	public static int getCalculatedDY() {
		return -(y - prey);
	}

	public static int getDX() {
		return dx;
	}

	public static int getDY() {
		return -(dy);
	}

	public static void setCursorPosition(int x, int y) {
		Mouse.setCursorPosition(x, DecisionTreeDrawer.SCREEN_HEIGHT - y);
	}

	public static void setGrabbed(boolean grabbed) {
		Mouse.setGrabbed(grabbed);
	}

}
