package eg.edu.guc.rolltheball.game.generic;

import java.util.Map;

public interface Problem {

	State getInitialState();
	boolean isGoal(State state);
	Map<Action, State> getAdjacentStates(State state);
	
	
}
