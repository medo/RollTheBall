package eg.edu.guc.rolltheball.impl;

import eg.edu.guc.rolltheball.generic.Operation;

public class OperationImpl extends Operation{

	int i;
	int j;
	Direction direction;
	
	public OperationImpl(int i, int j, Direction d) {
		this.i = i;
		this.j = j;
		this.direction = d;
	}
	
	@Override
	public String toString() {
		return "(" + i + "," + j + " )" + " => " + direction;
	}
	
}
