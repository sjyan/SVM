package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import libsvm.svm_model;
import main.SVM.Classes;
import main.SVM.Result;
import main.SVM.TestingImage;

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

		
		// TESTING W/ TINY IMAGE REPRESENTATION
		svm_model tinyImageLinearModelClutch = svm.trainSVM(Classes.CLUTCH, 4.5, .5, false, false);
		svm_model tinyImageLinearModelHobo = svm.trainSVM(Classes.HOBO, 2.5, .5, false, false);
		svm_model tinyImageLinearModelFlats = svm.trainSVM(Classes.FLATS, 3.3, .5, false, false);
		svm_model tinyImageLinearModelPumps = svm.trainSVM(Classes.PUMPS, 2.9, .5, false, false);
		
		svm_model tinyImageRBFModelClutch = svm.trainSVM(Classes.CLUTCH, 1, 3, true, false);
		svm_model tinyImageRBFModelHobo = svm.trainSVM(Classes.HOBO, .5, 4.1, true, false);
		svm_model tinyImageRBFModelFlats = svm.trainSVM(Classes.FLATS, 1.2, .4, true, false);
		svm_model tinyImageRBFModelPumps = svm.trainSVM(Classes.PUMPS, 1.2, 2, true, false);
		
		svm_model[] modelsToTest = {tinyImageLinearModelClutch, tinyImageLinearModelHobo, tinyImageLinearModelFlats, tinyImageLinearModelPumps,
				tinyImageRBFModelClutch, tinyImageRBFModelHobo, tinyImageRBFModelFlats, tinyImageRBFModelPumps};
		
		// Testing, evaluate the image under each model above and take the argmax over all the models
		TestingImage[] testImages = svm.getTestingImages();
		List<Result> results = new ArrayList<>();
		int predictRight = 0;
		int count = 0;
		for (TestingImage image : testImages) {
			
			results.add(svm.test(modelsToTest[0], image.image, false, Classes.CLUTCH));
			results.add(svm.test(modelsToTest[1], image.image, false, Classes.HOBO));
			results.add(svm.test(modelsToTest[2], image.image, false, Classes.FLATS));
			results.add(svm.test(modelsToTest[3], image.image, false, Classes.PUMPS));
			results.add(svm.test(modelsToTest[4], image.image, false, Classes.CLUTCH));
			results.add(svm.test(modelsToTest[5], image.image, false, Classes.HOBO));
			results.add(svm.test(modelsToTest[6], image.image, false, Classes.FLATS));
			results.add(svm.test(modelsToTest[7], image.image, false, Classes.PUMPS));
			
			double highestProb = 0.0;
			Result bestResult = null;
			for (Result r: results) {
				if (r.prob > highestProb) {
					highestProb = r.prob;
					bestResult = r;
				}
			}
			
			if (bestResult.classification == image.expectedClass) {
				predictRight++;
			}
			
			count++;
		}
		
		System.out.println("accuracy is " + (predictRight * 1.0) / count);
		
		// TESTING W/ HISTOGRAM REPRESENTATION

	}

}
