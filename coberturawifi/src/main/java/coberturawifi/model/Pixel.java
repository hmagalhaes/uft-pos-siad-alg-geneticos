package coberturawifi.model;

import java.awt.Color;

public class Pixel {

	public final int x;
	public final int y;
	public final Color color;

	public Pixel(int x, int y, int rgb) {
		this.x = x;
		this.y = y;
		this.color = new Color(rgb);
	}

	@Override
	public String toString() {
		final String red = Integer.toHexString(color.getRed());
		final String green = Integer.toHexString(color.getGreen());
		final String blue = Integer.toHexString(color.getBlue());

		return String.format("[%d,%d=#%.02s%.02s%.02s]", x, y, red, green, blue);
	}

	public boolean isEnabled() {
		if (color.getRed() > 0) {
			return false;
		}
		if (color.getGreen() > 0) {
			return false;
		}
		if (color.getBlue() > 0) {
			return false;
		}
		return true;
	}

	public boolean isIn(final Rect rect) {
		return x >= rect.x1 && x <= rect.x2 && y >= rect.y1 && y <= rect.y2;
	}

}
