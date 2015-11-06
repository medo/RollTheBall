package eg.edu.guc.rolltheball.search;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import eg.edu.guc.rolltheball.generic.Heuristic;
import eg.edu.guc.rolltheball.generic.Node;
import eg.edu.guc.rolltheball.generic.Problem;
import eg.edu.guc.rolltheball.generic.QueueStrategy;
import eg.edu.guc.rolltheball.generic.State;
import eg.edu.guc.rolltheball.impl.GreedyBoardHeuristic;

public class Greedy extends Search {
	
	static class Element implements Comparable<Element>{
		
		Node node;
		double cost;

		Element(Node n, double cost) {
			node = n;
			this.cost = cost;
		}
		
		@Override
		public int compareTo(Element e) {
			double cost1 = cost;
			double cost2 = e.cost;
			if (cost1 < cost2) return -1;
			if (cost1 > cost2) return 1;
			return 0;
		}
	}
	
	public static Greedy createInstance(Problem p) {
		return new Greedy(p, new GreedyBoardHeuristic());
	}
	
	public Greedy(Problem problem, final Heuristic heuristic) {
		super(problem, new QueueStrategy() {
			
			PriorityQueue<Element> queue = new PriorityQueue<Greedy.Element>();
			HashSet<State> visited = new HashSet<State>();
			int numberOfNodes = 1;
			
			@Override
			public boolean isDone() {
				return queue.isEmpty();
			}
			
			@Override
			public void enqueue(Node node) {
				Element e = new Element(node, heuristic.getCost(node.getState()));
				visited.add(node.getState());
				queue.add(e);
			}
			
			@Override
			public Node dequeue() {
				return queue.poll().node;
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
