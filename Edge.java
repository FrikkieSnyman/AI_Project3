public class Edge{
	private Double weight;
	private Double prevWeight;
	private Node fromNode;
	private Node toNode;

	public Edge(int fanin){
		weight = RandomGenerator.randomDouble((-1)/(Math.sqrt(fanin)),(1)/(Math.sqrt(fanin)));
		prevWeight = 0.0;
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

	public void updateAddWeight(Double weight){
		prevWeight = this.weight;
		this.weight += weight;
	}

	public Double getPrevWeight(){
		return prevWeight;
	}
}