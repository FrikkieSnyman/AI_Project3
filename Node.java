import java.util.LinkedList;

public class Node{
	private Integer type; //0 - input; 1 - hidden; 2 - output
	private LinkedList<Edge> incomingEdges;
	private LinkedList<Edge> outgoingEdges;
	// private Double input;
	private Double weightedSum = 0.0;
	Integer count;
	public Node(Integer type, Integer count){
		this.type = type;
		incomingEdges = new LinkedList<>();
		outgoingEdges = new LinkedList<>();
		this.count = count;
	}

	// public void setInput(Double input){
	// 	this.input = input;
	// }

	public void addToWeightedSum(Double weightedInput){
		weightedSum += weightedInput;
	}

	public LinkedList<Edge> getIncomingEdges(){
		return incomingEdges;
	}

	public LinkedList<Edge> getOutgoingEdges(){
		return outgoingEdges;
	}

	public void addToIncomingEdges(Edge edge){
		incomingEdges.add(edge);
	}

	public void addToOutgoingEdges(Edge edge){
		outgoingEdges.add(edge);
	}

	public Double activationFunction(){
		// This will essentailly produce the output for the node
		switch (type) {
			case 0:		// input node
				return weightedSum;
			case 1:		// hidden node
			case 2:		// output node
				return (1)/(1 + (Math.exp(-weightedSum)));
			default:
				break;
		}
		return 0.0;
	}
}