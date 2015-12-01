package eg.edu.guc.rolltheball.logic.resolution;

import java.util.List;
import java.util.Map;

import eg.edu.guc.rolltheball.game.generic.Action;
import eg.edu.guc.rolltheball.game.generic.Problem;
import eg.edu.guc.rolltheball.game.generic.State;
import eg.edu.guc.rolltheball.logic.grammer.Formula;

public class ResolutionProblem implements Problem{

	ResolutionState initialState;
	
	public ResolutionProblem(ResolutionState initialState) {
		this.initialState = initialState;
	}
	
	@Override
	public State getInitialState() {
		return this.initialState;
	}

	@Override
	public boolean isGoal(State state) {
		return state.isGoal();
	}

	@Override
	public Map<Action, State> getAdjacentStates(State state) {
		return state.getAdjacentStates();
	}

}
