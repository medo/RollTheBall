package eg.edu.guc.rolltheball.generic;

public class Node {

	Node parent;
	int depth;
	double cost;
	Action operator;
	State state;
	
	public Node(Node parent, int depth, double cost, Action operator, State state) {
		this.parent 	= parent;
		this.depth 		= depth;
		this.cost 		= cost;
		this.operator 	= operator;
		this.state 		= state;
	}

	public Node getParent() {
		return parent;
	}


	public int getDepth() {
		return depth;
	}

	public double getCost() {
		return cost;
	}

	public Action getOperator() {
		return operator;
	}

	public State getState() {
		return state;
	}
}
