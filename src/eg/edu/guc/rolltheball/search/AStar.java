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

public class AStar extends Search {
	
	static class Element implements Comparable<Element>{
		
		Node node;
		double cost;

		Element(Node n, double cost) {
			node = n;
			this.cost = cost;
		}
		
		@Override
		public int compareTo(Element e) {
			double cost1 = cost + node.getCost();
			double cost2 = e.cost + e.node.getCost();
			if (cost1 < cost2) return -1;
			if (cost1 > cost2) return 1;
			return 0;
		}
	}
	
	public AStar(Problem problem, final Heuristic heuristic) {
		super(problem, new QueueStrategy() {
			
			PriorityQueue<Element> queue = new PriorityQueue<AStar.Element>();
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
