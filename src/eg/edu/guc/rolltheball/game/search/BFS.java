package eg.edu.guc.rolltheball.game.search;

import java.util.HashMap;
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


public class BFS extends Search {

	
	public BFS(Problem problem) {
		super(problem, new QueueStrategy() {
			
			Queue<Node> queue = new LinkedList<Node>();
			HashSet<State> visited = new HashSet<State>();
			int numberOfNodes = 1;
			
			@Override
			public boolean isDone() {
				return queue.isEmpty();
			}
			
			@Override
			public void enqueue(Node node) {
				numberOfNodes++;
				visited.add(node.getState());
				queue.add(node);
			}
			
			@Override
			public Node dequeue() {
				return queue.poll();
			}

			@Override
			public int getNumberOfVistedNodes() {
				return numberOfNodes;
			}

			@Override
			public boolean isVisited(State state) {
				return visited.contains(state);
			}
		});
	}
}
