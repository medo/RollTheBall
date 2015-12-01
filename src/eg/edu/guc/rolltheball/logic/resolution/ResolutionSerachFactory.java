package eg.edu.guc.rolltheball.logic.resolution;

import eg.edu.guc.rolltheball.game.generic.Problem;
import eg.edu.guc.rolltheball.game.search.BFS;
import eg.edu.guc.rolltheball.game.search.Search;

public class ResolutionSerachFactory {

	static Search getInstance(Problem problem) {
		return new BFS(problem);
	}
	
}
