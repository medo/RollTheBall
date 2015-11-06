package eg.edu.guc.rolltheball.search;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import eg.edu.guc.rolltheball.generic.Node;
import eg.edu.guc.rolltheball.generic.Problem;
import eg.edu.guc.rolltheball.generic.QueueStrategy;
import eg.edu.guc.rolltheball.generic.State;

public class IterativeDeepening extends Search{

	public IterativeDeepening(Problem problem) {
		super(problem, new QueueStrategy() {
			
			Stack<Node> queue = new Stack<Node>();
			HashSet<State> visited = new HashSet<State>();
			int numberOfNodes = 1;
			int depth = 1;
			Node root;
			boolean rootNode = true;
			boolean treeExplored = true;
			
			@Override
			public boolean isDone() {
				return queue.isEmpty();
			}
			
			@Override
			public void enqueue(Node node) {
				if (rootNode) {
					rootNode = false;
					root = node;
				}
				if (node.getDepth() <= depth) {
					numberOfNodes++;
					visited.add(node.getState());
					queue.add(node);					
				} else {
					treeExplored = false;
				}
			}
			
			@Override
			public Node dequeue() {
				Node result = queue.pop();
				if (queue.isEmpty() && !treeExplored) {
					depth++;
					queue.push(root);
					visited = new HashSet<State>();
					treeExplored = true;
				}
				return result;
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
