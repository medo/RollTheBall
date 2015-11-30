package eg.edu.guc.rolltheball.game.main;

import eg.edu.guc.rolltheball.game.generic.Solution;
import eg.edu.guc.rolltheball.game.impl.BoardProblem;
import eg.edu.guc.rolltheball.game.search.BFS;
import eg.edu.guc.rolltheball.game.search.Search;

public class Main {

	public static void main(String[] args) {

		BoardProblem p = new BoardProblem(3, 3);
		System.out.println(p.getInitialState());
		Search b = new BFS(p);
		Solution s = b.search();
		if(s == null){
		    System.out.println("No Solution");
		}else{
		    System.out.println(s.toString());
		}
	}

}
