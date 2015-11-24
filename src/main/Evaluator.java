package main;

import java.awt.*;
import java.awt.image.*;
import libsvm.*;

import libsvm.*;

public class Evaluator {
	
	static final int VECSIZE = 32 * 32 * 3;
	static final int BINS = 8;
	static int[][][] RGB = new int[BINS][BINS][BINS];
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
	
	public static int[] concatenateImage(BufferedImage image) {
		int[] vec = new int[VECSIZE];
		int pixel;
		Color c;
		int count = 0;
		
		for(int i = 0; i < 32; i++) {
			for(int j = 0; j < 32; j++) {
				pixel = image.getRGB(i, j);
				c = new Color(pixel);
				
				// append RGB values per pixel
				vec[count] = c.getRed();
					count++;
				vec[count] = c.getGreen();
					count++;
				vec[count] = c.getBlue();
					count++;
			}
		}
		
		return vec;
	}
	
	public static svm_node[] convertAttributes(int[] attributes) {
		svm_node[] nodes = new svm_node[VECSIZE];
		
		for(int i = 0; i < attributes.length; i++) {
			svm_node node = new svm_node();
			node.index = i;
			node.value = attributes[i];
			nodes[i] = node;
		}
		
		return nodes;
	}
}
