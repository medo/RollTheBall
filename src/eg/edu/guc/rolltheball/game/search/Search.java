package eg.edu.guc.rolltheball.game.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import eg.edu.guc.rolltheball.game.generic.Action;
import eg.edu.guc.rolltheball.game.generic.Node;
import eg.edu.guc.rolltheball.game.generic.Problem;
import eg.edu.guc.rolltheball.game.generic.QueueStrategy;
import eg.edu.guc.rolltheball.game.generic.Solution;
import eg.edu.guc.rolltheball.game.generic.State;
import eg.edu.guc.rolltheball.game.util.Pair;

public class Search {

	Problem problem;
	QueueStrategy queue;
	
	public Search(Problem problem, QueueStrategy strategy) {
		this.problem = problem;
		this.queue = strategy;
	}
	
	protected List<Action> constructActionPath(Node node) {
	
		List<Action> result = new ArrayList<Action>();
		while (node.getParent() != null) {
			result.add(node.getOperator());
			node = node.getParent();
		}
		Collections.reverse(result);
		return result;
	}
	
	public Solution search() {
		Solution solution = null;
		Node initialNode = new Node(null, 0, 0, null, problem.getInitialState());
		queue.enqueue(initialNode);
		while (!queue.isDone()) {
			Node top = queue.dequeue();
			if (problem.isGoal(top.getState())) {
				List<Action> path = constructActionPath(top); 
				solution = new Solution(path, top.getCost(), queue.getNumberOfVistedNodes());
				break;
			}
			Map<Action, State> adj = problem.getAdjacentStates(top.getState());
			for (Action a : adj.keySet()) {
				State v;
				if (!queue.isVisited(v = adj.get(a))) {
					Node node = new Node(top, top.getDepth() + 1, top.getCost() + a.getCost(), a, v);
					queue.enqueue(node);
				}
			}
		}
		return solution;
	}
	
}
