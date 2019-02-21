package br.eti.hmagalhaes.coberturawifi.input;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import br.eti.hmagalhaes.coberturawifi.model.Pixel;

public class BitmapHelper {

	public BufferedImage readFile(final String fileName) throws IOException {
		final File file = new File(fileName);
		return ImageIO.read(file);
	}

	public List<Pixel> getPixels(final BufferedImage image) {
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

	public List<Pixel> getActivatedPixels(final BufferedImage image) {
		return getPixels(image).stream().filter(pixel -> pixel.isEnabled()).collect(Collectors.toList());
	}

}
