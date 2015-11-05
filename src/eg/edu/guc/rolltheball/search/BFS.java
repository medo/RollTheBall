package eg.edu.guc.rolltheball.search;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import eg.edu.guc.rolltheball.generic.Action;
import eg.edu.guc.rolltheball.generic.Node;
import eg.edu.guc.rolltheball.generic.Problem;
import eg.edu.guc.rolltheball.generic.QueueStrategy;
import eg.edu.guc.rolltheball.generic.Solution;
import eg.edu.guc.rolltheball.generic.State;
import eg.edu.guc.rolltheball.util.Pair;


public class BFS extends Search {

	
	public BFS(Problem problem) {
		super(problem, new QueueStrategy() {
			
			Queue<Node> queue = new LinkedList<Node>();
			
			@Override
			public boolean isDone() {
				return queue.isEmpty();
			}
			
			@Override
			public void enqueue(Node node) {
				queue.add(node);
			}
			
			@Override
			public Node dequeue() {
				return queue.poll();
			}
		});
	}
}
