package eg.edu.guc.rolltheball.logic.resolution;

import java.util.List;

import eg.edu.guc.rolltheball.game.generic.Solution;
import eg.edu.guc.rolltheball.game.search.Search;
import eg.edu.guc.rolltheball.logic.converters.CNF;
import eg.edu.guc.rolltheball.logic.grammer.And;
import eg.edu.guc.rolltheball.logic.grammer.Clause;
import eg.edu.guc.rolltheball.logic.grammer.Constant;
import eg.edu.guc.rolltheball.logic.grammer.Formula;
import eg.edu.guc.rolltheball.logic.grammer.Not;
import eg.edu.guc.rolltheball.logic.grammer.Or;
import eg.edu.guc.rolltheball.logic.grammer.Predicate;
import eg.edu.guc.rolltheball.logic.grammer.Term;

public class Resolution {
	
	Search search;
	List<Clause> formula;
	ResolutionProblem problem;
	
	public Resolution(List<Clause> cnf) {
		this.formula = cnf;
		this.problem = new ResolutionProblem(new ResolutionState(cnf));
		this.search = ResolutionSerachFactory.getInstance(problem);
	}
	
	boolean isContradiction() {
		Solution s = search.search();
		if (s != null) return true;
		return false;
	}
	
	
	public static void main(String[] args) {
		Formula test = new And(
				new Predicate("p"), new Not(new Predicate("p"))
				);
        List<Clause> cnf = new CNF().convertToCNF(test);
        Resolution r = new Resolution(cnf);
        System.out.println(r.isContradiction());
	}
	

}
