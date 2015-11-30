package eg.edu.guc.rolltheball.game.generic;

public interface QueueStrategy {

	void enqueue(Node node);
	Node dequeue();
	boolean isDone();
	int getNumberOfVistedNodes();
	boolean isVisited(State state);
	
}
