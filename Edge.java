public class Edge{
	private Double weight;
	private Node fromNode;
	private Node toNode;

	public Edge(int fanin){
		weight = RandomGenerator.randomDouble((-1)/(Math.sqrt(fanin)),(1)/(Math.sqrt(fanin)));
	}


	public Double getWeight(){
		return weight;
	}

	public void setWeight(Double weight){
		this.weight = weight;
	}
}