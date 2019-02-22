package br.eti.hmagalhaes.coberturawifi.input;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import br.eti.hmagalhaes.coberturawifi.Configs;
import br.eti.hmagalhaes.coberturawifi.model.Blueprint;
import br.eti.hmagalhaes.coberturawifi.model.Blueprint.PlantBuilder;
import br.eti.hmagalhaes.coberturawifi.model.Pixel;
import br.eti.hmagalhaes.coberturawifi.model.Rect;
import br.eti.hmagalhaes.coberturawifi.model.Rect.RectBuilder;
import br.eti.hmagalhaes.coberturawifi.model.Tile;

public class BlueprintReader {

	private static BlueprintReader instance;

	public static BlueprintReader getInstance() {
		if (instance == null) {
			instance = new BlueprintReader();
		}
		return instance;
	}

	public Blueprint readPlant(final String fileName, final int plantWidth, final int plantHeight) {
		final BufferedImage image = readImage(fileName);
		final Tiles tiles = createTiles(plantWidth, plantHeight, image);
		final int pixelsForMeter = image.getWidth() / plantWidth;

		return new PlantBuilder().widthInMeters(plantWidth).heightInMeters(plantHeight).widthInPixels(image.getWidth())
				.heightInPixels(image.getHeight()).requiredTileList(tiles.requiredTiles).allTileList(tiles.allTiles)
				.pixelsForMeter(pixelsForMeter).build();
	}

	private class Tiles {

		final List<Tile> allTiles = new ArrayList<>();
		final List<Tile> requiredTiles = new ArrayList<>();

	}

	private Tiles createTiles(final int plantWidth, final int plantHeight, final BufferedImage image) {
		final List<Pixel> activatedPixelList = getActivatedPixels(image);

		final byte tileSizeInMeters = Configs.getInstance().getByte(Configs.TILE_SIZE_IN_METERS);

		// regra de 3
		final int tileSizeInPixels = (tileSizeInMeters * image.getWidth()) / plantWidth;

		final Tiles tiles = new Tiles();

		for (int x = 0; x < image.getWidth(); x += tileSizeInPixels) {
			for (int y = 0; y < image.getHeight(); y += tileSizeInPixels) {

				final Rect rect = new RectBuilder().x(x).y(y).width(tileSizeInPixels).height(tileSizeInPixels).build();
				final Tile tile = new Tile(rect);

				tiles.allTiles.add(tile);

				final boolean coverabilityRequired = isAnyPixelInRect(activatedPixelList, rect);
				if (coverabilityRequired) {
					tiles.requiredTiles.add(tile);
				}
			}
		}
		return tiles;
	}

	private boolean isAnyPixelInRect(final List<Pixel> pixelList, final Rect rect) {
		for (Pixel pixel : pixelList) {
			if (pixel.isIn(rect)) {
				return true;
			}
		}
		return false;
	}

	private BufferedImage readImage(final String fileName) {
		final File file = new File(fileName);
		try {
			return ImageIO.read(file);
		} catch (IOException ex) {
			throw new RuntimeException("Error whilst reading plant file", ex);
		}
	}

	private List<Pixel> getPixels(final BufferedImage image) {
		final int height = image.getHeight();
		final int width = image.getWidth();

		final List<Pixel> pixelList = new ArrayList<>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final int rgb = image.getRGB(x, y);
				final Pixel pixel = new Pixel(x, y, rgb);
				pixelList.add(pixel);
			}
		}
		return pixelList;
	}

	private List<Pixel> getActivatedPixels(final BufferedImage image) {
		final Color requiredAreaColor = getRequiredAreaColor();
		return getPixels(image).stream().filter(pixel -> pixel.color.equals(requiredAreaColor))
				.collect(Collectors.toList());
	}

	private Color getRequiredAreaColor() {
		final String color = Configs.getInstance().getString(Configs.BLUEPRINT_REQUIRED_AREA_COLOR);
		return Color.decode(color);
	}

}