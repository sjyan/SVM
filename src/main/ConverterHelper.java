package main;

import java.awt.*;
import java.awt.image.*;
import libsvm.*;

import libsvm.*;

public class ConverterHelper {
	
	static final int VECSIZE = 32 * 32 * 3;
	static final int BINS = 8;
	static final int HSIZE = 8 * 8 * 8;
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
	
	public static double[] concatenateImage(BufferedImage image) {
		svm_node[] svmData;
		
		double[] vec = new double[VECSIZE];
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
		
//		svmData = convertAttributes(vec);
//		
//		return svmData;
	}
	
	public static svm_node[] convertVector(double[] vector) {
		svm_node[] nodes = new svm_node[VECSIZE + 1];
		
		for(int i = 0; i < vector.length; i++) {
			svm_node node = new svm_node();
			node.index = i+1;
			node.value = vector[i];
			nodes[i] = node;
		}
		
		svm_node node = new svm_node();
		node.index = -1;
		nodes[VECSIZE] = node; 
		return nodes;
	}
	
	public static svm_node[] convertHistogram(int[][][] colors) {
		svm_node[] nodes = new svm_node[HSIZE];
		
		for (int x=0; x<8; x++) {
		for (int y=0; y<8; y++) {
		for (int z=0; z<8; z++) {
			svm_node node = new svm_node();
			int index = x + y*8 + z*64;
			node.index = index;
			node.value = colors[x][y][z];
			nodes[index] = node;
		}
		}
		}
		svm_node node = new svm_node();
		node.index = -1;
		nodes[HSIZE-1] = node;
		return nodes;
	}
}
