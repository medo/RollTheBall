package eg.edu.guc.rolltheball.game.impl;

public enum Direction {

	NORTH(-1, 0, 0),
	EAST(0, 1, 1),
	SOUTH(1, 0, 2),
	WEST(0, -1, 3);

	public int i;
	public int j;
	public int direction;

	Direction(int i, int j, int direction) {
		this.i = i;
		this.j = j;
		this.direction = direction;
	}

	Direction opposite() {
		return getDirection(i * -1, j * -1);
	}

	static Direction getDirection(int i, int j) {
		if (i == -1 && j == 0) 	return NORTH;
		if (i == 1 	&& j == 0 ) return SOUTH;
		if (i == 0	&& j == 1) 	return EAST;
		if (i == 0 	&& j == -1) return WEST;
		return null;
	}

	static Direction getDirection(int i) {
		if (i == 0)	return NORTH;
		if (i == 1) return EAST;
		if (i == 2) return SOUTH;
		if (i == 3) return WEST;
		return null;
	}

	public boolean equals(Direction d) {
		return d.direction == direction;
	}

	@Override
    public String toString(){
	    if(direction == 0) {
	        return "^";
	    }else if(direction == 1){
	        return ">";
	    }else if(direction == 2){
	        return "v";
	    }else{
	        return "<";
	    }
	}

}
