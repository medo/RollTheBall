package eg.edu.guc.rolltheball.main;

import java.util.HashSet;

import eg.edu.guc.rolltheball.generic.Solution;
import eg.edu.guc.rolltheball.generic.State;
import eg.edu.guc.rolltheball.impl.Board;
import eg.edu.guc.rolltheball.impl.BoardProblem;
import eg.edu.guc.rolltheball.search.BFS;
import eg.edu.guc.rolltheball.search.DFS;
import eg.edu.guc.rolltheball.search.Search;

public class Main {

	public static void main(String[] args) {
		BoardProblem p = new BoardProblem(2, 2);
		Search b = new BFS(p);
		Solution s = b.search();
		System.out.println(s.toString());
	
		
		
	}
	
}
