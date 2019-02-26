package coberturawifi.model;

public class Coordinates {

	public final int x;
	public final int y;

	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinates other = (Coordinates) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Coordinates [x=" + x + ", y=" + y + "]";
	}

	public boolean isWithin(final Rect rect) {
		return this.x >= rect.x1 && this.x <= rect.x2 && this.y >= rect.y1 && this.y <= rect.y2;
	}

}
