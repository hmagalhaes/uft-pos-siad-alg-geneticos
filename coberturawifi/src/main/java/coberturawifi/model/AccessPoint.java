package coberturawifi.model;

public class AccessPoint {

	public final int x, y;
	public final int rangeRadius;

	public AccessPoint(int x, int y, int rangeRadius) {
		this.x = x;
		this.y = y;
		this.rangeRadius = rangeRadius;
	}

	@Override
	public String toString() {
		return "AccessPoint [x: " + x + ", y: " + y + "]";
	}

	public class AccessPointBuilder {
		private int x, y;
		private int rangeRadius;

		public AccessPoint build() {
			return new AccessPoint(x, y, rangeRadius);
		}

		public AccessPointBuilder x(int x) {
			this.x = x;
			return this;
		}

		public AccessPointBuilder y(int y) {
			this.y = y;
			return this;
		}

		public AccessPointBuilder rangeRadius(int rangeRadius) {
			this.rangeRadius = rangeRadius;
			return this;
		}

	}

}
