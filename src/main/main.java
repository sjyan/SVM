package main;

import java.io.IOException;

import libsvm.svm_model;
import main.SVM.Classes;

public class main {

	public main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		// test
		
//		BufferedImage image;
//		image = ImageHelper.readImage("images/clutch/img_bags_clutch_10.jpg");
		
//		int[][][] test;
//		test = Evaluator.sortImage(image);
//		Evaluator.printHistogram(test);
		
		SVM svm = new SVM();
		svm.initData();
		
		// TRAINING LINEAR KERNEL (TINY IMAGE)
		// Train Clutch
		//svm_model clutchModel = svm.trainSVM(Classes.CLUTCH);
		//svm.evaluate(Classes.CLUTCH, clutchModel);
		// c= 4.5
		// svm.trainAndEvaluateWithTuning(Classes.CLUTCH);
		// Train Hobo
//		svm.trainSVM(Classes.HOBO);
		// c= 2.5
		//svm.trainAndEvaluateWithTuning(Classes.HOBO);

//		// Train Flats
//		svm.trainSVM(Classes.FLATS);
		svm.trainAndEvaluateWithTuning(Classes.FLATS);

//		// Train Pumps
//		svm.trainSVM(Classes.PUMPS);
		
		// TRAINING RBF KERNEL (TINY IMAGE)
	}

}
