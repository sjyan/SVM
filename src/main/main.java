package main;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class main {

	public main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		// test
		BufferedImage image;
		image = ImageHelper.readImage("images/clutch/img_bags_clutch_10.jpg");
		
		int[][][] test;
		test = Evaluator.sortImage(image);
		Evaluator.printHistogram(test);
	}

}
