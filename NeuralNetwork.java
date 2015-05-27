import java.util.LinkedList;

public class NeuralNetwork{
	private LinkedList<Node> inputNodes;
	private LinkedList<Node> hiddenNodes;
	private LinkedList<Node> outputNodes;
	private LinkedList<Edge> inputToHidden;
	private LinkedList<Edge> hiddenToOutput;
	public Integer[] inputs;

	public NeuralNetwork(int numInputs, int numHidden, int numOutputs){
		inputNodes = new LinkedList<>();
		hiddenNodes = new LinkedList<>();
		outputNodes = new LinkedList<>();
		inputToHidden = new LinkedList<>();
		hiddenToOutput = new LinkedList<>();
		inputs = new Integer[numInputs];

		// Create input nodes
		for (int i = 0; i < numInputs; ++i){
			inputNodes.add(new Node(0,i));
		}
		// Input bias node
		Node tmpBias = new Node(0,99);
		tmpBias.addToWeightedSum(-1.0);
		inputNodes.add(tmpBias);

		// Create hidden nodes
		for (int i = 0; i < numHidden; ++i){
			hiddenNodes.add(new Node(1,i));
		}

		// Create edges between inputNodes and hiddenNodes, specifically placed here to avoid hidden bias
		for (Node inputNode : inputNodes){
			for (Node hiddenNode : hiddenNodes){
				Edge tmpEdge = new Edge(inputNodes.size());
				tmpEdge.setFromNode(inputNode);
				tmpEdge.setToNode(hiddenNode);
				inputToHidden.add(tmpEdge);
				inputNode.addToOutgoingEdges(tmpEdge);
				hiddenNode.addToIncomingEdges(tmpEdge);
			}
		}

		// Hidden bias node
		tmpBias = new Node(1,99);
		tmpBias.addToWeightedSum(-1.0);
		hiddenNodes.add(tmpBias);

		// Create output nodes
		for (int i = 0; i < numOutputs; ++i){
			outputNodes.add(new Node(2,i));
		}

		// Create edges between hiddenNodes and outputNodes
		for (Node hiddenNode : hiddenNodes){
			for (Node outputNode : outputNodes){
				Edge tmpEdge = new Edge(hiddenNodes.size());
				tmpEdge.setFromNode(hiddenNode);
				tmpEdge.setToNode(outputNode);
				hiddenToOutput.add(tmpEdge);
				hiddenNode.addToOutgoingEdges(tmpEdge);
				outputNode.addToIncomingEdges(tmpEdge);
			}
		}
	}

	public Double[] putThroughNetwork(int[] inputs){
		Double[] outputResults = new Double[outputNodes.size()];
		Double[] scaledInputs = scale(inputs);

		for (int i = 0; i < scaledInputs.length; ++i){
			Node inputNode = inputNodes.get(i);
			inputNode.addToWeightedSum(scaledInputs[i]);
		}

		for (Node hiddenNode : hiddenNodes){
			for (Edge edge : hiddenNode.getIncomingEdges()){
				Node input = edge.getFromNode();
				Double weight = edge.getWeight();
				hiddenNode.addToWeightedSum(input.activationFunction()*weight);
			}
		}

		int count = 0;
		for (Node outputNode : outputNodes){
			for (Edge edge : outputNode.getIncomingEdges()){
				Node hiddenNode = edge.getFromNode();
				Double weight = edge.getWeight();
				outputNode.addToWeightedSum(hiddenNode.activationFunction()*weight);
			}
			outputResults[count] = outputNode.activationFunction();
			count++;
		}
		return outputResults;
	}

	private Double[] scale(int[] inputs){
		Double[] scaledInputs = new Double[inputs.length];

		Double targetMax = Math.sqrt(3);
		Double targetMin = -targetMax;
		int min = inputs[0];
		int max = inputs[0];

		for (int i = 1; i < inputs.length; ++i){
			if (inputs[i] < min){
				min = inputs[i];
			} else if (inputs[i] > max){
				max = inputs[i];
			}
		}

		for (int i = 0; i < inputs.length; ++i){
			scaledInputs[i] = ((((double)inputs[i] - (double)min)/((double)max - (double)min))*(targetMax - targetMin))+(targetMin);
		}

		return scaledInputs;
	}
}