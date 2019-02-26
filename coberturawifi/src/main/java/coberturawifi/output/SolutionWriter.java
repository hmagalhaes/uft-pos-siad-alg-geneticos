package coberturawifi.output;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import coberturawifi.Configs;
import coberturawifi.model.AccessPoint;
import coberturawifi.model.Blueprint;
import coberturawifi.model.Layout;
import coberturawifi.model.Tile;

public class SolutionWriter {

	private static final String FILE_PATTERN_INDEX_TOKEN = "{index}";
	private static final String FILE_PATTERN_EXTENSION_TOKEN = "{extension}";
	private static final int CROSS_LENGTH_IN_PIXELS = 20;
	private static final CharSequence FILE_PATTERN_FOLDER_TOKEN = "{folder}";

	private static SolutionWriter instance;

	private final Color radiusColor;
	private final Color crossColor;
	private final Color gridColor;

	private SolutionWriter() {
		final Configs configs = Configs.getInstance();
		this.radiusColor = Color.decode(configs.getString(Configs.RESULT_COLOR_RADIUS));
		this.crossColor = Color.decode(configs.getString(Configs.RESULT_COLOR_CROSS));
		this.gridColor = Color.decode(configs.getString(Configs.RESULT_COLOR_GRID));
	}

	public static SolutionWriter getInstance() {
		if (instance == null) {
			instance = new SolutionWriter();
		}
		return instance;
	}

	public void printGridAllTiles(final String blueprintFile, final Blueprint blueprint, final String outputFolder) {
		final String outputFilePattern = Configs.getInstance().getString(Configs.RESULT_GRID_ALL_FILE_PATTERN);
		printGrid(blueprintFile, outputFilePattern, blueprint.allTileList, outputFolder);
	}

	public void printGridSelectedTiles(final String blueprintFile, final Blueprint blueprint,
			final String outputFolder) {
		final String outputFilePattern = Configs.getInstance().getString(Configs.RESULT_GRID_SELECTED_FILE_PATTERN);
		printGrid(blueprintFile, outputFilePattern, blueprint.requiredTileList, outputFolder);
	}

	public void printGrid(final String blueprintFile, final String outputFilePattern, final List<Tile> tileList,
			final String outputFolder) {

		final BufferedImage image = loadImage(blueprintFile);
		final Graphics2D graphics = image.createGraphics();

		graphics.setColor(gridColor);

		for (Tile tile : tileList) {
			final int x = tile.rect.x1;
			final int y = tile.rect.y1;
			final int width = tile.rect.x2 - tile.rect.x1;
			final int height = tile.rect.y2 - tile.rect.y1;

			graphics.drawRect(x, y, width, height);
		}

		final String fileFormat = Configs.getInstance().getString(Configs.RESULT_FILE_FORMAT);
		final String outputFile = outputFilePattern.replace(FILE_PATTERN_FOLDER_TOKEN, outputFolder)
				.replace(FILE_PATTERN_EXTENSION_TOKEN, fileFormat);
		System.out.printf("Salvando grid em => %s\n", outputFile);
		saveImage(image, outputFile, fileFormat);
	}

	public void printSolutions(final String blueprintFile, final List<Layout> layoutList, final String outputFolder) {

		final String fileFormat = Configs.getInstance().getString(Configs.RESULT_FILE_FORMAT);
		final String outputPattern = Configs.getInstance().getString(Configs.RESULT_SOLUTION_FILE_PATTERN);
		final String coveredTilesFileName = Configs.getInstance()
				.getString(Configs.RESULT_SOLUTION_COVERED_TILES_FILE_PATTERN);
		for (int layoutIndex = 0; layoutIndex < layoutList.size(); layoutIndex++) {
			final Layout layout = layoutList.get(layoutIndex);

			final BufferedImage image = loadImage(blueprintFile);
			final List<AccessPoint> accessPointList = layout.accessPointList;
			printSolutions(image, accessPointList);

			final String outputFile = outputPattern.replace(FILE_PATTERN_FOLDER_TOKEN, outputFolder)
					.replace(FILE_PATTERN_INDEX_TOKEN, Integer.toString(layoutIndex))
					.replace(FILE_PATTERN_EXTENSION_TOKEN, fileFormat);	
			System.out.printf("Salvando solução %d em => %s\n", layoutIndex, outputFile);
			saveImage(image, outputFile, fileFormat);

			final String coveredTilesFile = coveredTilesFileName.replace(FILE_PATTERN_FOLDER_TOKEN, outputFolder)
					.replace(FILE_PATTERN_INDEX_TOKEN, Integer.toString(layoutIndex))
					.replace(FILE_PATTERN_EXTENSION_TOKEN, fileFormat);
			printGrid(blueprintFile, coveredTilesFile, layout.coveredTileList, outputFolder);
		}
	}

	private void printSolutions(final BufferedImage image, final List<AccessPoint> accessPointList) {

		final Font font = new Font("serif", Font.BOLD, 12);

		final Graphics2D graphics = image.createGraphics();
		graphics.setFont(font);

		for (int i = 0; i < accessPointList.size(); i++) {
			final AccessPoint ap = accessPointList.get(i);
			drawRadius(ap, graphics);
//			drawCross(ap, graphics);
			drawNumber(ap, graphics, i);
		}
	}

	private void drawNumber(final AccessPoint ap, final Graphics2D graphics, final int number) {
		graphics.setColor(crossColor);
		graphics.drawString(Integer.toString(number), ap.x - 3, ap.y - 3);
	}

	private void drawCross(final AccessPoint ap, final Graphics2D graphics) {
		graphics.setColor(crossColor);

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
		final int doubleRadius = ap.rangeRadius * 2;

		final int x = ap.x - ap.rangeRadius;
		final int y = ap.y - ap.rangeRadius;

		graphics.setColor(radiusColor);
		graphics.fillOval(x, y, doubleRadius, doubleRadius);
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
