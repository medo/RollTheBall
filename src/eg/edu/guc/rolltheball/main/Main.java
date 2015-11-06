package eg.edu.guc.rolltheball.main;

import eg.edu.guc.rolltheball.generic.Solution;
import eg.edu.guc.rolltheball.impl.BoardProblem;
import eg.edu.guc.rolltheball.search.BFS;
import eg.edu.guc.rolltheball.search.Search;

public class Main {

	public static void main(String[] args) {
		BoardProblem p = new BoardProblem(4, 4);
		System.out.println(p.getInitialState());
		Search b = new BFS(p);
		Solution s = b.search();
		System.out.println(s.toString());
	}

}
