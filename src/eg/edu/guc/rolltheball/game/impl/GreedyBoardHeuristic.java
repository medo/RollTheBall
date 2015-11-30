package eg.edu.guc.rolltheball.game.impl;

import eg.edu.guc.rolltheball.game.generic.Heuristic;
import eg.edu.guc.rolltheball.game.generic.State;
import eg.edu.guc.rolltheball.game.util.Pair;

public class GreedyBoardHeuristic implements Heuristic {

	@Override
	public double getCost(State state) {
		Board b = (Board) state;
		int result = b.m * b.n;
		boolean visited[][] = new boolean[b.n][b.m];
		visited[b.startI][b.startJ] = true;
		Direction d = Tile.getOneDirection(Tile.decode(b.board[b.startI][b.startJ]));
		int i = b.startI + d.i;
		int j = b.startJ + d.j;
		while (i < b.n && i >= 0 && j < b.m && j >= 0) {
			if (i == b.goalI && j == b.goalJ){
				if (Tile.isGoal(Tile.decode(b.board[i][j])) && Tile.getOneDirection(Tile.decode(b.board[i][j])).equals(d.opposite())){
					return 0;
				}
				else break;
			}
			if (!Tile.isPath(Tile.decode(b.board[i][j]))) {
				break;
			}
			Pair<Direction, Direction> directions = Tile.getTwoDirections(Tile.decode(b.board[i][j]));
			boolean first = true;
			if (!directions.first.equals(d.opposite())){
				first = false;
				if (!directions.second.equals(d.opposite())) break;
			}
			i += first? directions.second.i : directions.first.i ;
			j += first? directions.second.j : directions.first.j ;
			d =  first? directions.second 	: directions.first;
			result--;
		}
		return result;
		
		
	}

	
	
}
