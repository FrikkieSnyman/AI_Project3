import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.AbstractMap.SimpleEntry;
import java.io.IOException;

public class Trainer{
	private NeuralNetwork nn;
	private Double learningRate;
	private Double momentum;
	private Integer maxEpoch;
	private Integer epoch = 0;
	private Double trainingSetPerc;
	private String pathToTraining;
	private int numI, numH, numO;

	public Trainer(Double learningRate, Double momentum, Integer maxEpoch, 
			Double trainingSetPerc, int numInputs, int numHidden, int numOutput, 
			String pathToTraining){

		this.learningRate = learningRate;
		this.momentum = momentum;
		this.maxEpoch = maxEpoch;
		this.trainingSetPerc = trainingSetPerc;
		this.pathToTraining = pathToTraining;
		this.numI = numInputs;
		this.numH = numHidden;
		this.numO = numOutput;
		nn = new NeuralNetwork(numInputs, numHidden, numOutput);
	}

	public void train(){
		String afrikaansFile = pathToTraining + "afrikaans.txt";
		String englishFile = pathToTraining + "english.txt";

		List<String> afrikaans = readFile(afrikaansFile);
		List<String> english = readFile(englishFile);

		LinkedList<SimpleEntry<String,String>> dataSet = buildList(afrikaans);
		dataSet.addAll(buildList(english));
		int accuracy;
		Boolean classified = true;

		for (; epoch < maxEpoch; ++epoch){
			accuracy = 0;
			int trainingAccuracy = 0;
			LinkedList<SimpleEntry<String,String>> trainingSet = splitIntoTrainingSet(dataSet).get(0);
			LinkedList<SimpleEntry<String,String>> generalizationSet = splitIntoTrainingSet(dataSet).get(1);
			int trainingSetSize = trainingSet.size();
			int generalizationSetSize = generalizationSet.size();
			int[] intResults;
			while (trainingSet.size() != 0){
				classified = true;
				int removeIndex = RandomGenerator.randomInteger(0, trainingSet.size()-1);
				SimpleEntry<String,String> data = trainingSet.remove(removeIndex);
				int[] inputs = determineFrequencies(data.getKey());
				Double[] results = nn.putThroughNetwork(inputs);
				intResults = new int[results.length];
				
				
				for (int i = 0; i < results.length; ++i){
					if (results[i] <= 0.3){
						intResults[i] = 0;
					} else if (results[i] >= 0.7){
						intResults[i] = 1;
					}
					else {
						// error classifying
						accuracy = 0;
						classified = false;
					}
				}
				// System.out.println(Arrays.toString(results) + " " + data.getValue());
				String language = data.getValue();
				if (classified){
					if (language.equals("e")){ // english
						if ((intResults[0] == 1) && (intResults[1] == 0)){
							// Correctly classified as english
							accuracy = 1;
						} else {
							accuracy = 0;
						}
					} else { // afrikaans
						if ((intResults[0] == 0) && (intResults[1] == 1)){
							// Correctly classified as Afrikaans
							accuracy = 1;
						} else {
							accuracy = 0;
						}
					}
				}

				trainingAccuracy += accuracy;

				Double[] outputErr = new Double[results.length];
				Double[] hiddenErr = new Double[nn.getHiddenNodes().size()];

				// calculate error for output nodes
				for (int i = 0; i < outputErr.length; ++i){
					Double target = 0.0;
					if (language.equals("e")){		// english
						if (i == 0){		// enlgish output node
							target = 1.0;
						}
					} else {		// afrikaans
						if (i == 1){
							target = 1.0;
						}
					}
					outputErr[i] = -(target - results[i])*(1.0 - results[i])*(results[i]);
				}

				// calculate error for hidden nodes
				for (int i = 0; i < hiddenErr.length; ++i){
					Node hidNode = nn.getHiddenNodes().get(i);
					LinkedList<Edge> outEdges = hidNode.getOutgoingEdges();
					hiddenErr[i] = 0.0;
					for (int j = 0; j < outEdges.size(); ++j){
						Double oe = outputErr[j];
						Double weight = outEdges.get(j).getWeight();
						hiddenErr[i] += (weight)*(oe)*(1.0-hidNode.activationFunction())*(hidNode.activationFunction());
					}
				}
				Double prevChange = 0.0;
				// calculate new weight for hidden to output
				LinkedList<Edge> hiddenToOutput = nn.getHiddenToOutput();
				for (Edge edge : hiddenToOutput){
					Double deltaWeight;
					Node output = edge.getToNode();
					Node hidden = edge.getFromNode();
					deltaWeight = -(learningRate)*(outputErr[output.count])*(hidden.activationFunction()) + (momentum*prevChange);
					edge.updateAddWeight(deltaWeight);
					prevChange = deltaWeight;
					// System.out.println(edge.getWeight());
				}

				nn.setHiddenToOutput(hiddenToOutput);
				prevChange = 0.0;
				// calculate new weight for input to hidden
				LinkedList<Edge> inputToHidden = nn.getInputToHidden();
				for (Edge edge : inputToHidden){
					Double deltaWeight;
					Node hidden = edge.getToNode();
					Node input = edge.getFromNode();
					if (hidden.count != -1){
						deltaWeight = -(learningRate)*(hiddenErr[hidden.count])*(input.activationFunction()) + (momentum*prevChange);
					} else {
						deltaWeight = -(learningRate)*(hiddenErr[hiddenErr.length-1])*(input.activationFunction()) + (momentum*prevChange);
					}
					edge.updateAddWeight(deltaWeight);
					prevChange = deltaWeight;
				}
				nn.setInputToHidden(inputToHidden);
			}

			Double accuracyPercentage = (double)trainingAccuracy / (double)trainingSetSize * 100.0;
			Double generalizationAccuracy = 0.0;

			while(generalizationSet.size() != 0){
				classified = true;
				int removeIndex = RandomGenerator.randomInteger(0, generalizationSet.size()-1);
				SimpleEntry<String,String> data = generalizationSet.remove(removeIndex);
				int[] inputs = determineFrequencies(data.getKey());
				Double[] results = nn.putThroughNetwork(inputs);
				intResults = new int[results.length];
				for (int i = 0; i < results.length; ++i){
					if (results[i] <= 0.3){
						intResults[i] = 0;
					} else if (results[i] >= 0.7){
						intResults[i] = 1;
					}
					else {
						// error classifying
						accuracy = 0;
						classified = false;
					}
				}

				String language = data.getValue();

				if (classified){
					if (language.equals("e")){ // english
						if ((intResults[0] == 1) && (intResults[1] == 0)){
							// Correctly classified as english
							accuracy = 1;
						} else {
							accuracy = 0;
						}
					} else { // afrikaans
						if ((intResults[0] == 0) && (intResults[1] == 1)){
							// Correctly classified as Afrikaans
							accuracy = 1;
						} else {
							accuracy = 0;
						}
					}
				}

				generalizationAccuracy += (double) accuracy;
			}

			generalizationAccuracy = generalizationAccuracy / generalizationSetSize * 100;
			// System.out.println(nn.getHiddenToOutput().get(0).getWeight() + " " + nn.getHiddenNodes().get(0).getOutgoingEdges().get(0).getWeight());
			System.out.println("Epoch: "+ epoch + "\nTraining accuracy: " + accuracyPercentage + "\nGeneralization accuracy: " + generalizationAccuracy);

		}
	}


	public NeuralNetwork getNeuralNetwork(){
		return nn;
	}

	private int[] determineFrequencies(String string){
		int[] returnThis = new int[26];
		string = string.toLowerCase();
		for (int i = 0; i < string.length(); ++i){
			char c = string.charAt(i);
			if ((c > 96 && c < 123)){
				returnThis[c-97]++;
			}
		}
		return returnThis;
	}

	private LinkedList<LinkedList<SimpleEntry<String,String>>> splitIntoTrainingSet(LinkedList<SimpleEntry<String,String>> dataSet){
		LinkedList<LinkedList<SimpleEntry<String,String>>> returnThis = new LinkedList<>();
		int numberOfItems = (int) Math.round(trainingSetPerc * dataSet.size());

		// First half of dataset is afrikaans, second half is english
		LinkedList<SimpleEntry<String,String>> tmp = new LinkedList<>();
		for (int i = 0; i < (int) Math.floor(numberOfItems/2); ++i){
			tmp.add(dataSet.get(i));
			tmp.add(dataSet.get(i + (int) (Math.floor((dataSet.size()/2))) ));
		}
		returnThis.add(tmp);

		tmp = new LinkedList<>();
		for (int i = (int) Math.floor(numberOfItems/2); i < (int) Math.floor(dataSet.size()/2); ++i){
			tmp.add(dataSet.get(i));
			tmp.add(dataSet.get(i + (int) (Math.floor((dataSet.size()/2))) ));	
		}
		returnThis.add(tmp);

		return returnThis;
	}

	private LinkedList<SimpleEntry<String, String>> buildList(List<String> file){
		LinkedList<SimpleEntry<String,String>> returnThis = new LinkedList<>();

		Iterator<String> text = file.iterator();
		while (text.hasNext()) {
			SimpleEntry<String, String> keyVal = new SimpleEntry<>(text.next(),text.next());
			returnThis.add(keyVal);
		}

		return returnThis;
	}

	private List<String> readFile(String path){
		List<String> lines = null;
		try{
			lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
		} catch(IOException e){
			e.printStackTrace();
		}
		return lines;
	}
}