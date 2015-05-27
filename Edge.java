public class Edge{
	private Double weight;
	private Node fromNode;
	private Node toNode;

	public Edge(int fanin){
		weight = RandomGenerator.randomDouble((-1)/(Math.sqrt(fanin)),(1)/(Math.sqrt(fanin)));
	}

	public Node getFromNode(){
		return fromNode;
	}

	public void setFromNode(Node fromNode){
		this.fromNode = fromNode;
	}

	public Node getToNode(){
		return toNode;
	}

	public void setToNode(Node toNode){
		this.toNode = toNode;
	}

	public Double getWeight(){
		return weight;
	}

	public void setWeight(Double weight){
		this.weight = weight;
	}
}