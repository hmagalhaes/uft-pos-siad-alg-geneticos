package coberturawifi.model;

import java.util.Collections;
import java.util.List;

public class Blueprint {

	public final int widthInPixels;
	public final int heightInPixels;
	public final int widthInMeters;
	public final int heightInMeters;
	public final List<Rect> requiredTileList;
	public final List<Rect> allTileList;
	public final int pixelsForMeter;

	public Blueprint(int widthInPixels, int heightInPixels, int widthInMeters, int heightInMeters,
			List<Rect> requiredTileList, List<Rect> allTileList, int pixelsForMeter) {

		this.widthInPixels = widthInPixels;
		this.heightInPixels = heightInPixels;
		this.widthInMeters = widthInMeters;
		this.heightInMeters = heightInMeters;
		this.requiredTileList = Collections.unmodifiableList(requiredTileList);
		this.allTileList = Collections.unmodifiableList(allTileList);
		this.pixelsForMeter = pixelsForMeter;
	}

	public static class BlueprintBuilder {

		private int widthInPixels;
		private int heightInPixels;
		private int widthInMeters;
		private int heightInMeters;
		private List<Rect> requiredTileList;
		private List<Rect> allTileList;
		private int pixelsForMeter;

		public Blueprint build() {
			return new Blueprint(widthInPixels, heightInPixels, widthInMeters, heightInMeters, requiredTileList,
					allTileList, pixelsForMeter);
		}

		public BlueprintBuilder widthInPixels(int widthInPixels) {
			this.widthInPixels = widthInPixels;
			return this;
		}

		public BlueprintBuilder heightInPixels(int heightInPixels) {
			this.heightInPixels = heightInPixels;
			return this;
		}

		public BlueprintBuilder widthInMeters(int widthInMeters) {
			this.widthInMeters = widthInMeters;
			return this;
		}

		public BlueprintBuilder heightInMeters(int heightInMeters) {
			this.heightInMeters = heightInMeters;
			return this;
		}

		public BlueprintBuilder requiredTileList(List<Rect> requiredTileList) {
			this.requiredTileList = requiredTileList;
			return this;
		}

		public BlueprintBuilder allTileList(List<Rect> allTileList) {
			this.allTileList = allTileList;
			return this;
		}

		public BlueprintBuilder pixelsForMeter(int pixelsForMeter) {
			this.pixelsForMeter = pixelsForMeter;
			return this;
		}

	}

}
