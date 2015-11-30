package eg.edu.guc.rolltheball.game.impl;

import java.util.Map;

import eg.edu.guc.rolltheball.game.generic.Action;
import eg.edu.guc.rolltheball.game.generic.Problem;
import eg.edu.guc.rolltheball.game.generic.State;

public class BoardProblem implements Problem {

	Board initBoard;
	
	public BoardProblem(int n, int m) {
		initBoard = Board.createBoard(n, m);
	}

	@Override
	public State getInitialState() {
		return initBoard;
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
