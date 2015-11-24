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
	
	List<List<BufferedImage>> trainingImages;
	List<List<BufferedImage>> testingImages;
	
	public SVM() throws IOException {
		// List containing training and testing images for each class. 
		// Classes.index corresponds to the index in each List
		// I.e. the List at index 1 in trainingImages corresponds to the training images of HOBO
		
//		List<List<Image>> trainingImages = new ArrayList<List<Image>>();
//		List<List<Image>> testingImages = new ArrayList<List<Image>>();
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
		
		List<List<Image>> negativeTrainingImages = new ArrayList<List<Image>>();
		for (int i = 0; i < NUM_CLASSES; i++) {
			List<Image> negatives = new ArrayList<>();
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
		
		// Tiny image representation
		
		
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
	
	public svm_model testClutchParams() {
		svm_problem prob = new svm_problem();
		
		List<BufferedImage> clutches = trainingImages.get(0);
		int numNodes = clutches.size()/2;
		
		double[] labels = new double[numNodes];
		for (int i=0; i<labels.length; i++) {
			labels[i] = 1;
		}
		
		svm_node[][] imageNodes = new svm_node[numNodes][];
		for (int i=0; i<numNodes; i++) {
			BufferedImage image = clutches.get(i);
			
			imageNodes[i] = ConverterHelper.convertAttributes(ConverterHelper.concatenateImage(image));
		}
		
		prob.l = numNodes;
		prob.y = labels;
		prob.x = imageNodes;
		
		
		svm_parameter param = new svm_parameter();
		param.kernel_type = svm_parameter.LINEAR;
		param.C = 1;
		
		svm_model model = svm.svm_train(prob, param);
		
		return model;
	}
	
	public double evaluate(svm_model model) {
		List<BufferedImage> clutches = trainingImages.get(0);
		int testNodes = (clutches.size()/2) + 1;
		
		BufferedImage image = clutches.get(testNodes);
		svm_node[] nodes;
		
		int[] attributes = ConverterHelper.concatenateImage(image);
		nodes = ConverterHelper.convertAttributes(attributes);
		
		int[] labels = new int[NUM_CLASSES];
		svm.svm_get_labels(model, labels);
		
	    double[] prob_estimates = new double[NUM_CLASSES];
	    double v = svm.svm_predict_probability(model, nodes, prob_estimates);

	    for (int i = 0; i < NUM_CLASSES; i++){
	        System.out.print("(" + labels[i] + ":" + prob_estimates[i] + ")");
	    }
	    System.out.println("(Actual:" + attributes[0] + " Prediction:" + v + ")");            

	    return v;
	}
}
