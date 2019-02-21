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

	public static class PlantBuilder {
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

		public PlantBuilder widthInPixels(int widthInPixels) {
			this.widthInPixels = widthInPixels;
			return this;
		}

		public PlantBuilder heightInPixels(int heightInPixels) {
			this.heightInPixels = heightInPixels;
			return this;
		}

		public PlantBuilder widthInMeters(int widthInMeters) {
			this.widthInMeters = widthInMeters;
			return this;
		}

		public PlantBuilder heightInMeters(int heightInMeters) {
			this.heightInMeters = heightInMeters;
			return this;
		}

		public PlantBuilder requiredTileList(List<Tile> requiredTileList) {
			this.requiredTileList = requiredTileList;
			return this;
		}

		public PlantBuilder allTileList(List<Tile> allTileList) {
			this.allTileList = allTileList;
			return this;
		}

		public PlantBuilder pixelsForMeter(int pixelsForMeter) {
			this.pixelsForMeter = pixelsForMeter;
			return this;
		}

	}

}
