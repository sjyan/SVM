package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageHelper {
	
	public static BufferedImage readImage(String location) throws IOException {
		BufferedImage bufferedImage = null;
		try {
		    bufferedImage = ImageIO.read(new File(location));			
		} catch(IOException e) {
			throw new IOException("Couldn't read in image");
		}
		return bufferedImage;
	}
	
	public static BufferedImage resizeImage(Image originalImage, int newX, int newY) {
		BufferedImage resized = null;
		if (originalImage != null) {
			resized = new BufferedImage(newX, newY, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = resized.createGraphics();
			g.drawImage(originalImage, 0, 0, newX, newY, null);
			g.dispose();
		}
		return resized;
	}
}
