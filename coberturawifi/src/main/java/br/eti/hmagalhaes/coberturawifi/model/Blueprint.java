package br.eti.hmagalhaes.coberturawifi.model;

import java.util.Collections;
import java.util.List;

public class Blueprint {

	public final int widthInPixels;
	public final int heightInPixels;
	public final int widthInMeters;
	public final int heightInMeters;
	public final List<Tile> requiredTileList;
	public final List<Tile> allTileList;
	public final int pixelsForMeter;

	public Blueprint(int widthInPixels, int heightInPixels, int widthInMeters, int heightInMeters,
			List<Tile> requiredTileList, List<Tile> allTileList, int pixelsForMeter) {
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
		private List<Tile> requiredTileList;
		private List<Tile> allTileList;
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

		public BlueprintBuilder requiredTileList(List<Tile> requiredTileList) {
			this.requiredTileList = requiredTileList;
			return this;
		}

		public BlueprintBuilder allTileList(List<Tile> allTileList) {
			this.allTileList = allTileList;
			return this;
		}

		public BlueprintBuilder pixelsForMeter(int pixelsForMeter) {
			this.pixelsForMeter = pixelsForMeter;
			return this;
		}

	}

}
