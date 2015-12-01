package eg.edu.guc.rolltheball.logic.resolution;

import java.util.HashMap;

import eg.edu.guc.rolltheball.game.generic.Action;
import eg.edu.guc.rolltheball.game.generic.Operation;
import eg.edu.guc.rolltheball.logic.grammer.Term;
import eg.edu.guc.rolltheball.logic.grammer.Variable;

public class ResolutionAction implements Action {

	public static int ID = 0;
	int id;
	Operation sub;
	
	public ResolutionAction(HashMap<Variable, Term> sub) {
		this.id = ID++;
		this.sub = new Substitution(sub);
	}
	
	@Override
	public double getCost() {
		return 1;
	}

	@Override
	public Operation getOperation() {
		return sub;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((ResolutionAction) obj).id == id;
	}

}
