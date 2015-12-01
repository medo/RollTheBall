package eg.edu.guc.rolltheball.logic.resolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eg.edu.guc.rolltheball.game.generic.Action;
import eg.edu.guc.rolltheball.game.generic.State;
import eg.edu.guc.rolltheball.logic.converters.Unificiation;
import eg.edu.guc.rolltheball.logic.grammer.Clause;
import eg.edu.guc.rolltheball.logic.grammer.Formula;
import eg.edu.guc.rolltheball.logic.grammer.Literal;
import eg.edu.guc.rolltheball.logic.grammer.Term;
import eg.edu.guc.rolltheball.logic.grammer.Variable;

public class ResolutionState implements State {

	List<Clause> formula;
	
	public ResolutionState(List<Clause> formula) {
		this.formula = formula;
	}
	
	public ResolutionState(ResolutionState state, Clause newClause) {
		formula = new ArrayList<Clause>();
		formula.addAll(state.formula);
		formula.add(newClause);
	}
	
	private boolean contains(Clause c) {
		for (Clause clause : formula)
			if (clause.toString().equals(c.toString()) || c.equals(clause)) return true;
		return false;
	}
	
	@Override
	public Map<Action, State> getAdjacentStates() {
		HashMap<Action, State> result = new HashMap<Action, State>();
		for (int i = 0; i < formula.size(); i++) {
			for (int j = i + 1; j < formula.size(); j++) {
				for (Literal lit1 : formula.get(i)) {
					for (Literal lit2 : formula.get(j)) {
						if (lit1.negated ^ lit2.negated) {
							HashMap<Variable, Term> sub = Unificiation.unify(lit1.predicate, lit2.predicate);
							if (sub == null) continue;
							Clause clause = new Clause();
							for (Literal l1 : formula.get(i)) if (!l1.toString().equals(lit1.toString())) clause.add(Unificiation.sub(sub, l1));
							for (Literal l2 : formula.get(j)) if (!l2.toString().equals(lit2.toString())) clause.add(Unificiation.sub(sub, l2));
							if (!contains(clause)) {
								//This is a new state
								System.out.println("STATE -> " + i + "," + j + " " +formula);
								Action a = new ResolutionAction(sub);
								State s = new ResolutionState(this, clause);
								result.put(a, s);
							}
						}	
					}
				}
			}
		}
		return result;
	}

	@Override
	public boolean isGoal() {
		for (Clause l : formula) {
			if (l.size() == 0) return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return formula.toString();
	}

}
