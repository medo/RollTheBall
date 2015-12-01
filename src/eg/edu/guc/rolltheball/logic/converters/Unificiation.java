package eg.edu.guc.rolltheball.logic.converters;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import eg.edu.guc.rolltheball.logic.grammer.Constant;
import eg.edu.guc.rolltheball.logic.grammer.Function;
import eg.edu.guc.rolltheball.logic.grammer.Literal;
import eg.edu.guc.rolltheball.logic.grammer.Predicate;
import eg.edu.guc.rolltheball.logic.grammer.Term;
import eg.edu.guc.rolltheball.logic.grammer.Variable;

public class Unificiation {
	// static ArrayList<Term[]> theta = new ArrayList<Term[]>();
	/*
	 * procedure unify(p, q, theta) Scan p and q left-to-right and find the
	 * first corresponding terms where p and q "disagree" ; where p and q not
	 * equal If there is no disagreement, return theta ; success Let r and s be
	 * the terms in p and q, respectively, where disagreement first occurs If
	 * variable(r) then theta = union(theta, {r/s}) unify(subst(theta, p),
	 * subst(theta, q), theta) else if variable(s) then theta =
	 * union(theta,{s/r}) unify(subst(theta, p), subst(theta, q), theta) else
	 * return "failure" end
	 */
	
	public static HashMap<Variable, Term> unify(Predicate p, Predicate q) {
		return unify_helper(p, q, new HashMap<Variable, Term>());
	}
	
	public static HashMap<Variable, Term> unify_helper(Predicate p, Predicate q, HashMap<Variable, Term> theta) {
		if (!q.name.equals(p.name)) {
			System.out.println(p.toString());
			System.out.println(q.toString());
			System.out.println("-------------------------------------");
			return null;
		}

		if (q.toString().equals(p.toString())) {
			System.out.println(p.toString());
			System.out.println(q.toString());
			System.out.println("-------------------------------------");
			return theta;
		}

		if ((q.terms.size()) != (p.terms.size())) {
			System.out.println(p.toString());
			System.out.println(q.toString());
			System.out.println("-------------------------------------");
			return null;
		}

		Term r = null;
		Term s = null;

		for (int i = 0; i < ((Predicate) p).terms.size(); i++) {
			r = ((Predicate) p).terms.get(i);
			s = ((Predicate) q).terms.get(i);

			if (!r.toString().equals(s)) {
				System.out.println(p.toString());
				System.out.println(q.toString());
				System.out.println("-------------------------------------");

				if (r instanceof Variable) {
					if (s instanceof Function)
						if (((Function) s).terms.get(0) instanceof Variable)
							if (((Variable) r).name.equals(((Variable) ((Function) s).terms.get(0)).name))
								break;

					theta.put((Variable) r, s);
					return unify_helper(subst(theta, p), subst(theta, q), theta);
				}

				if (s instanceof Variable) {
					if (r instanceof Function)
						if (((Function) r).terms.get(0) instanceof Variable)
							if (((Variable) s).name.equals(((Variable) ((Function) r).terms.get(0)).name))
								break;

					theta.put((Variable) s, r);
					return unify_helper(subst(theta, p), subst(theta, q), theta);
				}

				if ((r instanceof Function) && (s instanceof Function)
						&& !((Function) r).toString().equals(((Function) s).toString())) {
					if(substFunc(r, s, theta) != null)
					theta = substFunc(r, s, theta);
					return unify_helper(subst(theta, p), subst(theta, q), theta);

				}
			}
		}
		System.out.println(p.toString());
		System.out.println(q.toString());
		System.out.println("-------------------------------------");
		return null;
	}

	private static HashMap<Variable, Term> substFunc(Term r, Term s, HashMap<Variable, Term> theta) {
		if ((r instanceof Variable) && (s instanceof Variable)) {
			if (((Variable) r).name.equals(((Variable) s).name))
				return null;
			theta.put((Variable) s, r);
			return theta;
		}

		if ((r instanceof Variable) && (s instanceof Constant)) {
			theta.put((Variable) r, s);
			return theta;
		}

		if ((r instanceof Constant) && (s instanceof Variable)) {
			theta.put((Variable) s, r);
			return theta;
		}

		if ((r instanceof Function) && (s instanceof Function)) {
			if (!((Function) r).name.equals(((Function) s).name))
				return theta;
			substFunc(((Function) r).terms.get(0), ((Function) s).terms.get(0), theta);
		}
		return null;
	}

	public static Literal sub(HashMap<Variable, Term> theta, Literal l) {
		Literal result = new Literal();
		result.negated = l.negated;
		result.predicate = subst(theta, l.predicate);
		return result;
	}
	
	public static Predicate subst(HashMap<Variable, Term> theta, Predicate x) {
		for (Term term : x.terms) {
			if (term instanceof Variable) {
				if (theta != null && theta.get((Variable) term) != null) {
					x.terms.set(x.terms.indexOf(term), theta.get((Variable) term));
				}
			} else {
				if (term instanceof Function)
					sub(theta, (Function) term);
			}

		}
		return x;

	}

	public static Function sub(HashMap<Variable, Term> theta, Function f) {
		for (Term fTerm : f.terms) {
			if (fTerm instanceof Variable) {
				if (theta != null && theta.get((Variable) fTerm) != null) {
					f.terms.set(f.terms.indexOf(fTerm), theta.get((Variable) fTerm));
				}
				return f;
			}

			if (fTerm instanceof Function) {
				return sub(theta, (Function) fTerm);
			}
		}

		return f;
	}

	public static void main(String[] args) {
		// parents(x, father(x), mother(Bill)) & parents(Bill, father(Bill), y)
//		 Variable x = new Variable("x");
//		 Function father1 = new Function("father", x);
//		 Constant bill = new Constant("Bill");
//		 Function mother1 = new Function("mother", bill);
//		
//		 Predicate p = new Predicate("parents", new Term[] { x, father1, mother1
//		 });
//		
//		 Variable y = new Variable("y");
//		 Function father = new Function("father", bill);
//		 Predicate q = new Predicate("parents", new Term[] { bill, father, y });

		// P(x; g(x); g(f(a))) and P(f(u); v; v)

		// Variable x = new Variable("x");
		// Function gOfX = new Function("g", x);
		// Constant a = new Constant("a");
		// Function fOfA = new Function("f", a);
		// Function gOfF = new Function("g", fOfA);
		// Predicate p = new Predicate("p", new Term[] { x, gOfX, gOfF });
		//
		// Variable u = new Variable("u");
		// Variable v = new Variable("v");
		// Function fOfU = new Function("f", u);
		// Predicate q = new Predicate("p", new Term[] { fOfU, v, v });

		// P(a; y; f(y)) and P(z; z; u)

		Constant a = new Constant("a");
		Variable y = new Variable("y");
		Function f = new Function("f", y);
		Predicate p = new Predicate("p", new Term[] { a, y, f });

		Variable z = new Variable("z");
		Variable u = new Variable("u");
		Predicate q = new Predicate("p", new Term[] { z, z, u });

		// f(x; g(x); x) and f(g(u); g(g(z)); z)

//		 Variable x = new Variable("x");
//		 Function gOfX = new Function("g", x);
//		 Predicate p = new Predicate("f", new Term[] { x, gOfX, x });
//		
//		 Variable u = new Variable("u");
//		 Variable z = new Variable("z");
//		 Function gOfZ = new Function("g", z);
//		 Function gOfG = new Function("g", gOfZ);
//		 Function gOfU = new Function("g", u);
//		 Predicate q = new Predicate("f", new Term[] { gOfU, gOfG, z });
		HashMap<Variable, Term> answer = unify(p, q);
		if(answer != null)
			System.err.println(answer.toString());
		else 
			System.err.println("False");
	}

}
