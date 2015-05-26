import java.util.LinkedList;

public class Node{
	private Integer type; //0 - input; 1 - hidden; 2 - output
	private LinkedList<Edge> incomingEdges;
	private LinkedList<Edge> outgoingEdges;
	private Double input;

	public Node(Integer type){
		this.type = type;
	}

	public void setInput(Double input){
		this.input = input;
	}

	public void addToIncomingEdges(Edge edge){
		incomingEdges.add(edge);
	}
}