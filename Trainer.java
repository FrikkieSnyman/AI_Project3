public class Trainer{
	private NeuralNetwork nn;
	private Double learningRate;
	private Double momentum;
	private Integer maxEpoch;
	private Integer epoch = 0;

	public Trainer(Double learningRate, Double momentum, Integer maxEpoch, int numInputs, int numHidden, int numOutput){
		this.learningRate = learningRate;
		this.momentum = momentum;
		this.maxEpoch = maxEpoch;
		nn = new NeuralNetwork(numInputs, numHidden, numOutput);
	}

	public NeuralNetwork getNeuralNetwork(){
		return nn;
	}
}