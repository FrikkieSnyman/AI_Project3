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

	public Trainer(Double learningRate, Double momentum, Integer maxEpoch, 
			Double trainingSetPerc, int numInputs, int numHidden, int numOutput, 
			String pathToTraining){

		this.learningRate = learningRate;
		this.momentum = momentum;
		this.maxEpoch = maxEpoch;
		this.trainingSetPerc = trainingSetPerc;
		this.pathToTraining = pathToTraining;

		nn = new NeuralNetwork(numInputs, numHidden, numOutput);
	}

	public void train(){
		String afrikaansFile = pathToTraining + "afrikaans.txt";
		String englishFile = pathToTraining + "english.txt";

		List<String> afrikaans = readFile(afrikaansFile);
		List<String> english = readFile(englishFile);

		LinkedList<SimpleEntry<String,String>> dataSet = buildList(afrikaans);
		dataSet.addAll(buildList(english));

		LinkedList<SimpleEntry<String,String>> trainingSet = splitIntoTrainingSet(dataSet).get(0);
		int[] intResults;
		while (trainingSet.size() != 0){
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
					// error classifying sentence
				}
			}
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