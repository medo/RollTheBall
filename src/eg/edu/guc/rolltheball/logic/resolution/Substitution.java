package eg.edu.guc.rolltheball.logic.resolution;

import java.util.HashMap;




import eg.edu.guc.rolltheball.game.generic.Operation;
import eg.edu.guc.rolltheball.logic.grammer.Term;
import eg.edu.guc.rolltheball.logic.grammer.Variable;

public class Substitution extends Operation {

	HashMap<Variable, Term> sub;
	
	public Substitution(HashMap<Variable, Term> sub) {
		this.sub = sub;
	}
	
	@Override
	public String toString() {
		return sub.toString();
	}
	
}
