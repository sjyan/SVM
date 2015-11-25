package main;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import libsvm.*;

public class SVM {
	
	public enum Classes {
		CLUTCH("images/clutch", 0),
		HOBO("images/hobo", 1),
		FLATS("images/flats", 2),
		PUMPS("images/pumps", 3);
		public String folderLocation;
		public int index;
		Classes(String folderLocation, int index){
			this.folderLocation = folderLocation;
			this.index = index;
		};
	}
	
	private static final int NUM_CLASSES = 4;
	private static final double PERCENTAGE_TRAINING = .70;
	private static final double DEFAULT_GAMMA = 0.5;

	// List containing training and testing images for each class. 
	// Classes.index corresponds to the index in each List
	// I.e. the List at index 1 in trainingImages corresponds to the training images of HOBO
	List<List<BufferedImage>> trainingImages;
	List<List<BufferedImage>> testingImages;
	List<List<BufferedImage>> negativeTrainingImages;
	
	public SVM() throws IOException {}
	
	public void initData() throws IOException {
		trainingImages = new ArrayList<List<BufferedImage>>();
		testingImages = new ArrayList<List<BufferedImage>>();

		// Initialize the List of Lists
		// I don't think it's necessary to do this now oh well refactor later
		for (int i = 0; i < NUM_CLASSES; i++) {
			trainingImages.add(i, new ArrayList<BufferedImage>());
			testingImages.add(i, new ArrayList<BufferedImage>());
		}
		
		// Iterate through the classes
		for (Classes c: Classes.values()) {
			File directory = new File(c.folderLocation);
			
			// Get all the images
			List<BufferedImage> images = new ArrayList<>();
			for (File imageFile : directory.listFiles()) {
				images.add(ImageIO.read(imageFile));
			}
			
			// Split number of Training and Testing data, 70% and 30% respectively
			int numberOfTrainingData = (int) (images.size() * PERCENTAGE_TRAINING);
			int numberOfTestingData = images.size() - numberOfTrainingData;
			
			// Need to randomly choose the test data
			List<Integer> randomIndexes = generateRandomIndexes(images,
					numberOfTestingData);
			
			// Add the training and test data
			for (int i = 0; i < images.size(); i++) {
				if (randomIndexes.contains(i)) {
					testingImages.get(c.index).add(images.get(i));
				} else {
					trainingImages.get(c.index).add(images.get(i));
				}
			}
		}
		
		// Get full training data for each class
		// So far, we have only got the positive examples from each class
		// For a good training set, we also need to include negative examples
		// We are going to train a one vs all SVM for each class
		// This means when training an SVM for a class, we will include negatives 
		// 	from the other 3 classes.
		// Our negative examples will be composed of 1/3 from each of the other 3 classes
		// Our full training data will be 50% positive and 50% negative
		
		negativeTrainingImages = new ArrayList<List<BufferedImage>>();
		for (int i = 0; i < NUM_CLASSES; i++) {
			List<BufferedImage> negatives = new ArrayList<>();
			for (int j = 0; j < NUM_CLASSES; j++) {
				if (i != j) {
					List<Integer> randomIndexes = generateRandomIndexes(trainingImages.get(j),
							trainingImages.get(i).size() / 3);
					for (int k : randomIndexes) {
						negatives.add(trainingImages.get(j).get(k));
					}
				}
 			}
			negativeTrainingImages.add(negatives);
		}	
	}
	
	public void testAndEvaluate() {
		
	}

	private <T> List<Integer> generateRandomIndexes(List<T> images,
			int numberOfTestingData) {
		List<Integer> randomIndexes = new ArrayList<>();
		for (int i = 0; i < numberOfTestingData; i++) {
			int random = (int) (Math.random() * images.size());
			while (randomIndexes.contains(random)) {
				random = (int) (Math.random() * images.size());
			}
			randomIndexes.add(random);
		}
		return randomIndexes;
	}
	
	public svm_model trainSVM(Classes clazz, double c, double g, boolean isRBF) {
		
		svm_problem prob = new svm_problem();
		List<BufferedImage> images = trainingImages.get(clazz.index);
		int numNodes = images.size();
		double[] labels = new double[numNodes];
		// One VS. All SVM
		for (int i = 0; i < numNodes / 2; i++) {
			labels[i] = 1;
		}
		
		for (int i = numNodes/2; i < numNodes; i++) {
			labels[i] = -1;
		}
		
		// Get positive examples (50%)
		//Array of tuples
		svm_node[][] imageNodes = new svm_node[numNodes][];
		for (int i = 0; i < numNodes / 2; i++) {
			BufferedImage image = images.get(i);
			imageNodes[i] = ConverterHelper.convertVector(ConverterHelper.concatenateImage(image));
		}
		
		// Get negative examples (50%)
		List<BufferedImage> clutchNegatives = negativeTrainingImages.get(0);
		for (int i = numNodes / 2; i < numNodes; i++) {
			int randomIndex = (int) (Math.random() * numNodes / 2);
			BufferedImage image = clutchNegatives.get(randomIndex);
			imageNodes[i] = ConverterHelper.convertVector(ConverterHelper.concatenateImage(image));
		}
		
		prob.l = numNodes;
		prob.y = labels;
		prob.x = imageNodes;
		
		//Setting parameters for SVM
		svm_parameter param = new svm_parameter();
		if (isRBF) {
			param.kernel_type = svm_parameter.RBF;

		} else {
			param.kernel_type = svm_parameter.LINEAR;

		}
		param.C = c;
		param.nu = 0.5;
		param.cache_size = 20000;
		param.eps =.001;
		param.gamma = g;
		param.probability = 1;
		param.probability = 1;
		
		svm_model model = svm.svm_train(prob, param);
		return model;
	}
	
	//Evaluation for SVM Model Linear
	public void trainAndEvaluateWithTuningLinear(Classes clazz) {
		for (double i = .1; i < 10; i = i + .4) {
			svm_model model = trainSVM(clazz, i, DEFAULT_GAMMA, false);
			evaluateLinear(clazz, model, i, -1);
		}
	}
	
	// Evaluation for SVM Model RBF
	public void trainAndEvaluateWithTuningRBF(Classes clazz) {
		for (double i = .1; i < 10; i = i + .4) {
			for (double g = .1; g < 5; g = g + .4) {
				svm_model model = trainSVM(clazz, i, g, true);
				evaluateLinear(clazz, model, i, g);
			}
		}
	}
	
	public double evaluateLinear(Classes clazz, svm_model model, double cValue, double gValue) {
		List<BufferedImage> images = trainingImages.get(0);
		
		int numOfPositiveTrainingImages = trainingImages.get(clazz.index).size();
		int numOfTuningImages = numOfPositiveTrainingImages / 2;
		int numCorrect = 0;
		for (int i = numOfTuningImages; i < numOfPositiveTrainingImages; i++) {
			BufferedImage image = images.get(i);
			
			svm_node[] nodes;
			double[] vector = ConverterHelper.concatenateImage(image);
			nodes = ConverterHelper.convertVector(vector);
			double[] prob_estimates = new double[2];
			double v = svm.svm_predict_probability(model, nodes, prob_estimates);
			if ((int) v == 1) {
				numCorrect++;
			}  
		}
		double percentageCorrect = (numCorrect * 1.0) / numOfTuningImages;
		System.out.println("The c value is " + cValue);
		if (gValue != -1) {
			System.out.println("The g value is  " + gValue);
		}
		System.out.println("The percentage predicted correct is " + percentageCorrect);
	    return percentageCorrect;
	}
	
	public double test(svm_model model) {
		return 0.0;
	}
}
