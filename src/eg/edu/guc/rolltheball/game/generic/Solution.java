package eg.edu.guc.rolltheball.game.generic;

import java.util.List;

public class Solution {

	
	private List<Action> actions;
	private double totalCost;
	private int numberOfNodes;
	
	public Solution(List<Action> actions, double totalCost, int numberOfNodes) {
		this.actions = actions;
		this.totalCost = totalCost;
		this.numberOfNodes = numberOfNodes;
	}
	
	List<Action> getActions() {
		return this.actions;
	}
	
	double getCost() {
		return this.totalCost;
	}
	
	int getNumberOfNodesExpanded() {
		return this.numberOfNodes;
	}
	
	@Override
	public String toString() {
		String result = "";
		result += "Total Cost = " + totalCost + System.lineSeparator();
		result += "Number of Nodes expanded = " + numberOfNodes + System.lineSeparator();
		result += "Actions:" + System.lineSeparator() + System.lineSeparator();
		for (Action a : actions)
			result += a.getOperation().toString() + System.lineSeparator();
		return result;
	}
	
}
