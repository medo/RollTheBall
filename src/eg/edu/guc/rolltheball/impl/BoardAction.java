package eg.edu.guc.rolltheball.impl;

import eg.edu.guc.rolltheball.generic.Action;
import eg.edu.guc.rolltheball.generic.Operation;

public class BoardAction implements Action {

	private OperationImpl operation;

	public BoardAction(OperationImpl o) {
		this.operation = o;
	}
	
	@Override
	public double getCost() {
		return 1;
	}

	@Override
	public Operation getOperation() {
		return operation;
	}

}
