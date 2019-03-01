package coberturawifi.input;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import coberturawifi.Configs;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Blueprint.BlueprintBuilder;
import coberturawifi.model.Pixel;
import coberturawifi.model.Rect;
import coberturawifi.model.Rect.RectBuilder;
import coberturawifi.util.ImageHelper;

public class BlueprintReader {

	private static BlueprintReader instance;

	public static BlueprintReader getInstance() {
		if (instance == null) {
			instance = new BlueprintReader();
		}
		return instance;
	}

	public Blueprint readPlant() {
		final String fileName = Configs.getInstance().getString(Configs.BLUEPRINT_INPUT_FILE);
		final int blueprintWidth = Configs.getInstance().getInt(Configs.BLUEPRINT_WIDTH_IN_METERS);
		final int blueprintHeight = Configs.getInstance().getInt(Configs.BLUEPRINT_HEIGHT_IN_METERS);

		System.out.println("Lendo planta => arquivo: " + fileName + ", largura: " + blueprintWidth + "m, altura: "
				+ blueprintHeight + "m");

		final BufferedImage image = ImageHelper.readImage(fileName);
		final Rects Rects = createRects(blueprintWidth, blueprintHeight, image);
		final int pixelsForMeter = image.getWidth() / blueprintWidth;

		return new BlueprintBuilder().widthInMeters(blueprintWidth).heightInMeters(blueprintHeight)
				.widthInPixels(image.getWidth()).heightInPixels(image.getHeight()).requiredTileList(Rects.requiredRects)
				.allTileList(Rects.allRects).pixelsForMeter(pixelsForMeter).build();
	}

	private class Rects {

		final List<Rect> allRects = new ArrayList<>();
		final List<Rect> requiredRects = new ArrayList<>();

	}

	private Rects createRects(final int plantWidth, final int plantHeight, final BufferedImage image) {
		final List<Pixel> activatedPixelList = getRequiredPixels(image);

		final float RectSizeInMeters = Configs.getInstance().getFloat(Configs.TILE_SIZE_IN_METERS);

		// regra de 3
		final int RectSizeInPixels = (int) ((RectSizeInMeters * image.getWidth()) / plantWidth);

		final Rects rects = new Rects();

		for (int x = 0; x < image.getWidth(); x += RectSizeInPixels) {
			for (int y = 0; y < image.getHeight(); y += RectSizeInPixels) {

				final Rect rect = new RectBuilder().x(x).y(y).width(RectSizeInPixels).height(RectSizeInPixels).build();

				rects.allRects.add(rect);

				final boolean coverabilityRequired = isAnyPixelInRect(activatedPixelList, rect);
				if (coverabilityRequired) {
					rects.requiredRects.add(rect);
				}
			}
		}
		return rects;
	}

	private boolean isAnyPixelInRect(final List<Pixel> pixelList, final Rect rect) {
		for (Pixel pixel : pixelList) {
			if (pixel.isIn(rect)) {
				return true;
			}
		}
		return false;
	}

	private List<Pixel> getRequiredPixels(final BufferedImage image) {
		final int requiredAreaColor = getRequiredAreaColor().getRGB();
		final int height = image.getHeight();
		final int width = image.getWidth();

		final List<Pixel> pixelList = new ArrayList<>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final int rgb = image.getRGB(x, y);
				if (rgb == requiredAreaColor) {
					final Pixel pixel = new Pixel(x, y, rgb);
					pixelList.add(pixel);
				}
			}
		}
		return pixelList;
	}

	private Color getRequiredAreaColor() {
		final String color = Configs.getInstance().getString(Configs.BLUEPRINT_REQUIRED_AREA_COLOR);
		return Color.decode(color);
	}

}
