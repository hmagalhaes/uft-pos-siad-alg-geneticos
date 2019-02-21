package br.eti.hmagalhaes.coberturawifi.output;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import br.eti.hmagalhaes.coberturawifi.Configs;
import br.eti.hmagalhaes.coberturawifi.model.AccessPoint;
import br.eti.hmagalhaes.coberturawifi.model.Blueprint;
import br.eti.hmagalhaes.coberturawifi.model.Layout;
import br.eti.hmagalhaes.coberturawifi.model.Tile;

public class SolutionWriter {

	private static final String FILE_PATTERN_INDEX_TOKEN = "{index}";
	private static final String FILE_PATTERN_EXTENSION_TOKEN = "{extension}";
	private static final Color RADIUS_COLOR = Color.YELLOW;
	private static final Color CROSS_COLOR = Color.RED;
	private static final int CROSS_LENGTH_IN_PIXELS = 20;

	private static SolutionWriter instance;

	public static SolutionWriter getInstance() {
		if (instance == null) {
			instance = new SolutionWriter();
		}
		return instance;
	}

	public void printGridAllTiles(final String blueprintFile, final Blueprint blueprint) {
		final String outputFilePattern = Configs.getInstance().getString(Configs.RESULT_GRID_ALL_FILE_PATTERN);
		printGrid(blueprintFile, outputFilePattern, blueprint.allTileList);
	}

	public void printGridSelectedTiles(final String blueprintFile, final Blueprint blueprint) {
		final String outputFilePattern = Configs.getInstance().getString(Configs.RESULT_GRID_SELECTED_FILE_PATTERN);
		printGrid(blueprintFile, outputFilePattern, blueprint.requiredTileList);
	}

	public void printGrid(final String blueprintFile, final String outputFilePattern, final List<Tile> tileList) {

		final BufferedImage image = loadImage(blueprintFile);
		final Graphics2D graphics = image.createGraphics();

		graphics.setColor(RADIUS_COLOR);

		for (Tile tile : tileList) {
			final int x = tile.rect.x1;
			final int y = tile.rect.y1;
			final int width = tile.rect.x2 - tile.rect.x1;
			final int height = tile.rect.y2 - tile.rect.y1;

			graphics.drawRect(x, y, width, height);
		}

		final String fileFormat = Configs.getInstance().getString(Configs.RESULT_FILE_FORMAT);
		final String outputFile = outputFilePattern.replace(FILE_PATTERN_EXTENSION_TOKEN, fileFormat);
		System.out.printf("Salvando grid em => %s\n", outputFile);
		saveImage(image, outputFile, fileFormat);
	}

	public void printSolutions(final String blueprintFile, final List<Layout> layoutList) {

		final String fileFormat = Configs.getInstance().getString(Configs.RESULT_FILE_FORMAT);
		final String outputPattern = Configs.getInstance().getString(Configs.RESULT_SOLUTION_FILE_PATTERN);
		for (int layoutIndex = 0; layoutIndex < layoutList.size(); layoutIndex++) {

			final BufferedImage image = loadImage(blueprintFile);
			final List<AccessPoint> accessPointList = layoutList.get(layoutIndex).accessPointList;
			printSolutions(image, accessPointList);

			final String outputFile = outputPattern.replace(FILE_PATTERN_INDEX_TOKEN, Integer.toString(layoutIndex))
					.replace(FILE_PATTERN_EXTENSION_TOKEN, fileFormat);
			System.out.printf("Salvando solução %d em => %s\n", layoutIndex, outputFile);
			saveImage(image, outputFile, fileFormat);
		}
	}

	private void printSolutions(final BufferedImage image, final List<AccessPoint> accessPointList) {

		final Graphics2D graphics = image.createGraphics();
		for (AccessPoint ap : accessPointList) {
			drawRadius(ap, graphics);
			drawCross(ap, graphics);
		}
	}

	private void drawCross(final AccessPoint ap, final Graphics2D graphics) {
		graphics.setColor(CROSS_COLOR);

		final int x1 = ap.x - (CROSS_LENGTH_IN_PIXELS / 2);
		final int x2 = ap.x + (CROSS_LENGTH_IN_PIXELS / 2);
		final int y1 = ap.y - (CROSS_LENGTH_IN_PIXELS / 2);
		final int y2 = ap.y + (CROSS_LENGTH_IN_PIXELS / 2);

		graphics.drawLine(x1, y1, x2, y2);
		graphics.drawLine(x1, y1 + 1, x2, y2 + 1);
		graphics.drawLine(x1, y1 - 1, x2, y2 - 1);

		graphics.drawLine(x1, y2, x2, y1);
		graphics.drawLine(x1, y2 + 1, x2, y1 + 1);
		graphics.drawLine(x1, y2 - 1, x2, y1 - 1);
	}

	private void drawRadius(final AccessPoint ap, final Graphics2D graphics) {
		final int x = ap.x - (ap.rangeRadius / 2);
		final int y = ap.y - (ap.rangeRadius / 2);

		graphics.setColor(RADIUS_COLOR);
		graphics.fillOval(x, y, ap.rangeRadius, ap.rangeRadius);
	}

	private void saveImage(final BufferedImage image, final String outputFile, final String fileFormat) {
		try (OutputStream os = new FileOutputStream(outputFile)) {
			ImageIO.write(image, fileFormat, os);
		} catch (IOException ex) {
			throw new RuntimeException("Unable to save solution file", ex);
		}
	}

	private BufferedImage loadImage(final String plantFileName) {
		try (InputStream is = new FileInputStream(plantFileName)) {
			return ImageIO.read(is);
		} catch (IOException ex) {
			throw new RuntimeException("Unable to load plant file", ex);
		}
	}

}
