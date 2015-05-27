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
		tmpBias.setInput(-1.0);
		inputNodes.add(tmpBias);

		// Create hidden nodes
		for (int i = 0; i < numHidden; ++i){
			hiddenNodes.add(new Node(1,i));
		}

		// Create edges between inputNodes and hiddenNodes, specifically placed here to avoid hidden bias
		for (Node inputNode : inputNodes){
			for (Node hiddenNode : hiddenNodes){
				Edge tmpEdge = new Edge(inputNodes.size());
				inputToHidden.add(tmpEdge);
				inputNode.addToOutgoingEdges(tmpEdge);
				hiddenNode.addToIncomingEdges(tmpEdge);
			}
		}

		// Hidden bias node
		tmpBias = new Node(1,99);
		tmpBias.setInput(-1.0);
		hiddenNodes.add(tmpBias);

		// Create output nodes
		for (int i = 0; i < numOutputs; ++i){
			outputNodes.add(new Node(2,i));
		}

		// Create edges between hiddenNodes and outputNodes
		for (Node hiddenNode : hiddenNodes){
			for (Node outputNode : outputNodes){
				Edge tmpEdge = new Edge(hiddenNodes.size());
				hiddenToOutput.add(tmpEdge);
				hiddenNode.addToOutgoingEdges(tmpEdge);
				outputNode.addToIncomingEdges(tmpEdge);
			}
		}
	}
}