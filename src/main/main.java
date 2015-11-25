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
		//svm.evaluate(Classes.CLUTCH, clutchModel);
		// c= 4.5, .992
		// svm.trainAndEvaluateWithTuningLinear(Classes.CLUTCH, false);
		
		// Train Hobo
		// c= 2.5, .987
		//svm.trainAndEvaluateWithTuningLinear(Classes.HOBO, false);

//		// Train Flats
		// c = 3.3, .992
//		svm.trainAndEvaluateWithTuningLinear(Classes.FLATS, false);

//		// Train Pumps
//		svm.trainSVM(Classes.PUMPS);
		//svm.trainAndEvaluateWithTuningLinear(Classes.PUMPS, false);
		// c = 2.9, .988

		// TRAINING RBF KERNEL (TINY IMAGE)
		// scores generally higer than the linear kernel counterpart
		
		// Train Clutch
		// there were many instances where accuracy was 100%
//		svm.trainAndEvaluateWithTuningRBF(Classes.CLUTCH, false);
		
		// Train Hobo
		// c-.5, g =4.1 yielded 100%, other combination of parameters also did too
		//svm.trainAndEvaluateWithTuningRBF(Classes.HOBO, false);
		
		// Train Flats
		// c = 1.2, g = .4
		// svm.trainAndEvaluateWithTuningRBF(Classes.FLATS, false);
		
		// Train Pumps
		// c = 1.2, g = 2
		// svm.trainAndEvaluateWithTuningRBF(Classes.PUMPS, false);

		
		
		
		
		// TRAIN WITH HISTOGRAM LINEAR 
		// Training Clutch
		// 100% probability in a lot of cases, very good 
		// svm.trainAndEvaluateWithTuningLinear(Classes.CLUTCH, true);
		
		// Training Hobo
		// svm.trainAndEvaluateWithTuningLinear(Classes.HOBO, true);
		
		// Training Flats
		// svm.trainAndEvaluateWithTuningLinear(Classes.FLATS, true);

		
		// Training Pumps
		// svm.trainAndEvaluateWithTuningLinear(Classes.PUMPS, true);
		
		// TRAINING WITH HISTOGRAM RBF
		
		// Train clutch
		 // svm.trainAndEvaluateWithTuningRBF(Classes.CLUTCH, true);
		
		// Train Hobo
		//svm.trainAndEvaluateWithTuningRBF(Classes.HOBO, true);
		
		// Train Flats
		// svm.trainAndEvaluateWithTuningRBF(Classes.FLATS, true);
		
		// Train Pumps
		// svm.trainAndEvaluateWithTuningRBF(Classes.PUMPS, true);


	}

}
