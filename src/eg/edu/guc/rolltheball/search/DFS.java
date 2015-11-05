package eg.edu.guc.rolltheball.search;

import java.util.Stack;

import eg.edu.guc.rolltheball.generic.Node;
import eg.edu.guc.rolltheball.generic.Problem;
import eg.edu.guc.rolltheball.generic.QueueStrategy;

public class DFS extends Search{

	public DFS(Problem problem) {
		super(problem, new QueueStrategy() {
			
			Stack<Node> stack = new Stack<Node>();
			
			@Override
			public boolean isDone() {
				return stack.isEmpty();
			}
			
			@Override
			public void enqueue(Node node) {
				stack.add(node);
			}
			
			@Override
			public Node dequeue() {
				return stack.pop();
			}
		});
	}
	
}
