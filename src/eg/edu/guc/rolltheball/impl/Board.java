package eg.edu.guc.rolltheball.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import eg.edu.guc.rolltheball.generic.Action;
import eg.edu.guc.rolltheball.generic.State;
import eg.edu.guc.rolltheball.util.Pair;

public class Board implements State {

	int[][] board;
	int goalI;
	int goalJ;
	int startI;
	int startJ;
	int n;
	int m;

	public static Board createBoard(int n, int m) {
		return new Board(4, 4, 1, 1, 1, 3);
		//return new Board(n,m, false);
	}

	private Board(Board b) {
		this(b.n, b.m, b.startI, b.startJ, b.goalI, b.goalJ);
	}

	private Board(int n, int m, int sI, int sJ, int gI, int gJ) {
		this.startI = sI;
		this.startJ = sJ;
		this.goalI = gI;
		this.goalJ = gJ;
		this.n = n;
		this.m = m;

		board = generateRandomBoard(n, m, true);
	}

	private Board(int n, int m, boolean keepStarts) {
		this.m = m;
		this.n = n;
		board = new int[n][m];
		//Should be random
		board[0][0] = Tile.encode(Tile.getBlock());
		board[0][1] = Tile.encode(Tile.getBlock());
		board[0][2] = Tile.encode(Tile.getBlock());
		board[0][3] = Tile.encode(Tile.getBlock());
		board[1][0] = Tile.encode(Tile.getBlock());
		board[1][1] = Tile.encode(Tile.getInitial(Direction.SOUTH));
		board[1][2] = Tile.encode(Tile.getBlock());
		board[1][3] = Tile.encode(Tile.getGoal(Direction.SOUTH));
		board[2][0] = Tile.encode(Tile.getPath(Direction.NORTH, Direction.EAST, true));
		board[2][1] = Tile.encode(Tile.getPath(Direction.NORTH, Direction.SOUTH, true));
		board[2][2] = Tile.encode(Tile.getPath(Direction.NORTH, Direction.SOUTH, true));
		board[2][3] = Tile.encode(Tile.getBlank());
		board[3][0] = Tile.encode(Tile.getBlank());
		board[3][1] = Tile.encode(Tile.getBlank());
		board[3][2] = Tile.encode(Tile.getPath(Direction.WEST, Direction.EAST, false));
		board[3][3] = Tile.encode(Tile.getPath(Direction.WEST, Direction.NORTH, false));
		
		//board = generateRandomBoard(n, m, keepStarts);
	}

	void setBoard(int[][] board) {
		this.board = board;
	}

	@Override
	public Map<Action, State> getAdjacentStates() {
		Map<Action, State> result = new HashMap<Action, State>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				List<Direction> directions = getPossibleDirection(i, j);
				if (directions != null) {
					for (Direction d : directions) {
						BoardAction a = new BoardAction(new OperationImpl(i, j, d));
						State s = applyOperation(i, j, d);
						result.put(a, s);
					}
				}
			}
		}
		return result;
	}

	State applyOperation(int i, int j, Direction d) {
		int newI = i + d.i;
		int newJ = j + d.j;
		int tmp = board[newI][newJ];
		int[][] newBoard = new int[n][m];
		for (int x = 0; x < n; x++) for (int y = 0; y < m; y++) newBoard[x][y] = board[x][y];
		newBoard[newI][newJ] = newBoard[i][j];
		newBoard[i][j] = tmp;
		Board result = new Board(this);
		result.setBoard(newBoard);
		return result;
	}

	List<Direction> getPossibleDirection(int i, int j) {
		List<Direction> result = null;
		if (Tile.isMovable(Tile.decode(board[i][j]))) {
			result = new ArrayList<Direction>();
			if (i - 1 >= 0 && Tile.isBlank(Tile.decode(board[i - 1][j]))) {
				result.add(Direction.NORTH);
			}
			if (j + 1 < m && Tile.isBlank(Tile.decode(board[i][j + 1]))) {
				result.add(Direction.EAST);
			}
			if (i + 1 < n && Tile.isBlank(Tile.decode(board[i + 1][j]))) {
				result.add(Direction.SOUTH);
			}
			if (j - 1 >= 0 && Tile.isBlank(Tile.decode(board[i][j - 1]))) {
				result.add(Direction.WEST);
			}
		}
		return result;
	}

	@Override
	public boolean isGoal() {
		boolean visited[][] = new boolean[n][m];
		visited[startI][startJ] = true;
		Direction d = Tile.getOneDirection(Tile.decode(board[startI][startJ]));
		int i = startI + d.i;
		int j = startJ + d.j;
		while (i < n && i >= 0 && j < m && j >= 0) {
			if (i == goalI && j == goalJ){
				if (Tile.isGoal(Tile.decode(board[i][j])) && Tile.getOneDirection(Tile.decode(board[i][j])).equals(d.opposite())){
					return true;
				}
				else break;
			}
			if (!Tile.isPath(Tile.decode(board[i][j]))) {
				break;
			}
			Pair<Direction, Direction> directions = Tile.getTwoDirections(Tile.decode(board[i][j]));
			boolean first = true;
			if (!directions.first.equals(d.opposite())){
				first = false;
				if (!directions.second.equals(d.opposite())) return false;
			}
			i += first? directions.second.i : directions.first.i ;
			j += first? directions.second.j : directions.first.j ;
			d =  first? directions.second 	: directions.first;
		}
		return false;

	}

	@Override
	public boolean equals(Object obj) {
		Board b = (Board) obj;
		return Arrays.deepEquals(board, b.board);
	}

	@Override
	public String toString() {
		char[][] b = new char[3*n][3*m];
		for (int i = 0; i < 3*n; i+=3) {
			for (int j = 0; j < 3*m; j+=3) {
			    String tmp = Tile.decode(board[i/3][j/3]);
			    char[][] x = Tile.visualize(tmp);
			    for(int q=0;q<3;q++)
			        for(int w=0;w<3;w++)
			            b[i+q][j+w] = x[q][w];
			}
		}
		String newLine = "";
		for(int i=0;i<3*m;i++)
		    newLine += " ";
		newLine += "\n";
		String result = "";
		for(int i=0;i<b.length;i++){
		    if(i != 0 && i%3 == 0)
                result+= newLine;
		    for(int j=0;j<b[0].length;j++){
		        if(j != 0 && j%3 == 0)
                    result+= ' ';
		        result += b[i][j];
		    }

		    result += System.lineSeparator();
		}
		return result;
	}


	@Override
	public int hashCode() {
		//TODO optimize
		String result = "";
		for (int i = 0; i < n; i++) for (int j = 0; j < m; j++) result += board[i][j];
		return result.hashCode();


	}

	public int[][] generateRandomBoard(int n, int m, boolean keepStarts){
	    Random r = new Random();
	    Direction[] d = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

	    int[][] b2 = new int[n][m];

	    for(int i=0;i<n;i++){
	        for(int j=0;j<m;j++){
	            int tileType = r.nextInt(3);
	            if(tileType == 0){
	                b2[i][j] = Tile.encode(Tile.getBlock());
	            }else if(tileType == 1){
	                b2[i][j] = Tile.encode(Tile.getBlank());
	            }else if(tileType == 2){
	                int dir1 = r.nextInt(4);
	                int dir2 = dir1;
	                while(dir2 == dir1)
	                    dir2 = r.nextInt(4);
	                boolean movable = r.nextBoolean();
	                b2[i][j] = Tile.encode(Tile.getPath(d[dir1], d[dir2], movable));
	            }
	        }
	    }


	    if(!keepStarts){
    	    goalI = r.nextInt(n);
    	    goalJ = r.nextInt(m);
    	    do{
    	        startI = r.nextInt(n);
    	        startJ = r.nextInt(m);
    	    }while(startI == goalI && goalJ == startJ);
	    }
	    b2[goalI][goalJ] = Tile.encode(Tile.getGoal(d[r.nextInt(4)]));
	    b2[startI][startJ] = Tile.encode(Tile.getInitial(d[r.nextInt(4)]));
        return b2;
	}

}
