import java.util.LinkedList;

public class Node{
	private Integer type; //0 - input; 1 - hidden; 2 - output
	private LinkedList<Edge> incomingEdges;
	private LinkedList<Edge> outgoingEdges;
	private Double input;
	private Double weightedSum = 0.0;
	Integer count;
	public Node(Integer type, Integer count){
		this.type = type;
		incomingEdges = new LinkedList<>();
		outgoingEdges = new LinkedList<>();
		this.count = count;
	}

	public void setInput(Double input){
		this.input = input;
	}

	public void addToWeightedSum(Double weightedInput){
		weightedSum += weightedInput;
	}

	public void addToIncomingEdges(Edge edge){
		incomingEdges.add(edge);
	}

	public void addToOutgoingEdges(Edge edge){
		outgoingEdges.add(edge);
	}

	public Double activationFunction(){
		// This will essentailly produce the output for the node
		return 0.0;
	}
}