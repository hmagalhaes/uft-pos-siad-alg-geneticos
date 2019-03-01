package coberturawifi.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ImageHelper {

	public static BufferedImage readImage(final String fileName) {
		final File file = new File(fileName);
		try {
			return ImageIO.read(file);
		} catch (IOException ex) {
			throw new RuntimeException("Error whilst reading plant file", ex);
		}
	}

	public static void saveImage(final BufferedImage image, final String outputFile, final String fileFormat) {
		try (OutputStream os = new FileOutputStream(outputFile)) {
			ImageIO.write(image, fileFormat, os);
		} catch (IOException ex) {
			throw new RuntimeException("Unable to save solution file", ex);
		}
	}

}
