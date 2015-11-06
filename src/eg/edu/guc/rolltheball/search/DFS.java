package eg.edu.guc.rolltheball.search;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import eg.edu.guc.rolltheball.generic.Node;
import eg.edu.guc.rolltheball.generic.Problem;
import eg.edu.guc.rolltheball.generic.QueueStrategy;
import eg.edu.guc.rolltheball.generic.State;

public class DFS extends Search{

	public DFS(Problem problem) {
		super(problem, new QueueStrategy() {
			
			Stack<Node> queue = new Stack<Node>();
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
				return queue.pop();
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
