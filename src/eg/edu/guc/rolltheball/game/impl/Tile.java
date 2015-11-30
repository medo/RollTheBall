package eg.edu.guc.rolltheball.game.impl;

import eg.edu.guc.rolltheball.game.util.Pair;

public class Tile {

	final static char INI_BLOCK 	= 'O';
	final static char INI_PATH 		= 'P';
	final static char INI_INITIAL 	= 'I';
	final static char INI_BLANK 	= 'A';
	final static char INI_GOAL 		= 'G';

	static TileType getTileType(String tile) {
		TileType result = null;
		char repLetter = tile.charAt(0);
		if (repLetter == INI_BLOCK) {
			result = TileType.BLOCK;
		} else if (repLetter == INI_PATH) {
			result = TileType.PATH;
		} else if (repLetter == INI_INITIAL) {
			result = TileType.INITIAL;
		} else if (repLetter == INI_GOAL) {
			result = TileType.GOAL;
		} else if (repLetter == INI_BLANK) {
			result = TileType.BLANK;
		}
		return result;
	}

	//0 => -1, 0
	//1 => 0, 1
	//2 => 1, 0
	//3 => 0, -1

	static Direction getOneDirection(String tile) {
		return Direction.getDirection(tile.charAt(1) - '0');
	}

	static Pair<Direction, Direction> getTwoDirections(String tile) {
		return new Pair<Direction, Direction>(Direction.getDirection(tile.charAt(1) - '0')
				, Direction.getDirection(tile.charAt(2) - '0'));
	}

	static String getBlock() {
		return "" + INI_BLOCK ;
	}

	static String getBlank() {
		return "" + INI_BLANK;
	}

	static String getPath(int x, int y, boolean movable) {
		return "" + INI_PATH + x + y + (movable? "M" : "");
	}

	static String getGoal(int x) {
		return "" + INI_GOAL + x;
	}

	static String getInitial(int x) {
		return "" + INI_INITIAL + x;
	}

	static String getPath(Direction first, Direction second, boolean movable) {
		return "" + INI_PATH + first.direction + second.direction + (movable? "M" : "");
	}

	static String getGoal(Direction d) {
		return "" + INI_GOAL + d.direction;
	}

	static String getInitial(Direction d) {
		return "" + INI_INITIAL + d.direction;
	}

	static boolean isBlock(String tile) {
		return tile.charAt(0) == INI_BLOCK;
	}

	static boolean isBlank(String tile) {
		return tile.charAt(0) == INI_BLANK;
	}

	static boolean isPath(String tile) {
		return tile.charAt(0) == INI_PATH;
	}

	static boolean isGoal(String tile) {
		return tile.charAt(0) == INI_GOAL;
	}

	static boolean isInitial(String tile) {
		return tile.charAt(0) == INI_INITIAL;
	}

	static boolean isMovable(String tile) {
		return isBlock(tile) || tile.charAt(tile.length() - 1) == 'M';
	}

	static int encode(String tile) {
		int result = -1;
		if (isBlock(tile)) {
			result = 1;
		} else if (isBlank(tile)) {
			result = 2;
		} else if (isInitial(tile)) {
			result = 3 + getOneDirection(tile).direction;
		} else if (isGoal(tile)) {
			result = 7 + getOneDirection(tile).direction;
		} else if (isPath(tile)) {
			Pair<Direction, Direction> pair = getTwoDirections(tile);
			int base = 11;
			if (isMovable(tile)) base = 1000;
			result = base + pair.first.direction + (pair.second.direction * 4);
		}
		return result;
	}

	static String decode(int x) {
		if (x <= 1) {
			return getBlock();
		} else if (x <= 2) {
			return getBlank();
		} else if (x <= 6) {
			return getInitial(x - 3);
		} else if (x <= 10) {
			return getGoal(x - 7);
		} else {
			int base = 11;
			boolean movable = false;
			if (x >= 1000){
				base = 1000;
				movable = true;
			}
			x -= base;
			return getPath(x % 4, x / 4, movable);
		}
	}

	public static char[][] visualize(String tile){
	    char[][] ret = new char[3][3];
	    for(int i=0;i<3;i++)
	        for(int j=0;j<3;j++)
	            ret[i][j] = '.';
	    if(Tile.isBlock(tile)){
	        ret[1][1] = 'X';
	    }else if(Tile.isGoal(tile)){
	        ret[1][1] = 'G';

	    }else if(Tile.isInitial(tile)){
	        ret[1][1] = 'I';
	    }

	    if(Tile.isGoal(tile) || Tile.isInitial(tile)){
	        Direction d = Tile.getOneDirection(tile);
	        if(d == Direction.NORTH){
	            ret[0][1] = '^';
	        }else if(d == Direction.EAST){
	            ret[1][2] = '>';
	        }else if(d == Direction.SOUTH){
	            ret[2][1] = 'v';
	        }else{
	            ret[1][0] = '<';
	        }
	    }else if(Tile.isPath(tile)){
	        Pair<Direction, Direction> pd = Tile.getTwoDirections(tile);
	        Direction d = pd.first;
	        if(d == Direction.NORTH){
                ret[0][1] = '^';
            }else if(d == Direction.EAST){
                ret[1][2] = '>';
            }else if(d == Direction.SOUTH){
                ret[2][1] = 'v';
            }else{
                ret[1][0] = '<';
            }
	        d = pd.second;
	        if(d == Direction.NORTH){
                ret[0][1] = '^';
            }else if(d == Direction.EAST){
                ret[1][2] = '>';
            }else if(d == Direction.SOUTH){
                ret[2][1] = 'v';
            }else{
                ret[1][0] = '<';
            }
	        if(!Tile.isMovable(tile)){
	            ret[1][1] = 'X';
	        }
	    }
	    return ret;
	}
}
