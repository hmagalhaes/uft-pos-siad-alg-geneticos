package br.eti.hmagalhaes.coberturawifi.model;

public class Rect {

	public final int x1, y1;
	public final int x2, y2;

	public Rect(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public String toString() {
		return String.format("Rect [%d, %d, %d, %d]", x1, y1, x2, y2);
	}

	public boolean collidesWith(final Coordinates coords) {
		return coords.x >= x1 && coords.x <= x2 && coords.y >= y1 && coords.y <= y2;
	}

	public static class RectBuilder {

		private int x, y;
		private int width, height;

		public RectBuilder x(int x) {
			this.x = x;
			return this;
		}

		public RectBuilder y(int y) {
			this.y = y;
			return this;
		}

		public RectBuilder width(int width) {
			this.width = width;
			return this;
		}

		public RectBuilder height(int height) {
			this.height = height;
			return this;
		}

		public Rect build() {
			final int x2 = x + width;
			final int y2 = y + height;
			return new Rect(x, y, x2, y2);
		}
	}

}
