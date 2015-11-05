package eg.edu.guc.rolltheball.generic;

import java.util.List;
import java.util.Map;

public interface State {
	
	Map<Action, State> getAdjacentStates();
	boolean isGoal();
	
}
