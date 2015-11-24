package main;

import java.awt.*;
import java.awt.image.*;

import libsvm.*;

public class Evaluator {
	
	static int[][][] RGB = new int[8][8][8];
	static int width, height;
	
	public static int[][][] sortImage(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		int pixel;
		Color c;
		
		for (int i=0; i<width; i++) {
		for (int j=0; j<height; j++) {
			pixel = image.getRGB(i, j);
			c = new Color(pixel);
			
			RGB[c.getRed()/32][c.getGreen()/32][c.getBlue()/32] += 1;
		}
		}
		
		return RGB;
	}
	
	public static void printHistogram(int[][][] colors) {
		String s = "";
		
		for (int x=0; x<8; x++) {
		for (int y=0; y<8; y++) {
		for (int z=0; z<8; z++) {
			s += colors[x][y][z] + " ";
		}
			System.out.println(s);
			s = "";
		}
			System.out.println("-----------------");
		}
	}
	
	
}
