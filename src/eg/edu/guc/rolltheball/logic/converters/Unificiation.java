package eg.edu.guc.rolltheball.logic.converters;

import java.util.ArrayList;
import java.util.List;

import eg.edu.guc.rolltheball.logic.grammer.Constant;
import eg.edu.guc.rolltheball.logic.grammer.Formula;
import eg.edu.guc.rolltheball.logic.grammer.Function;
import eg.edu.guc.rolltheball.logic.grammer.Predict;
import eg.edu.guc.rolltheball.logic.grammer.Term;
import eg.edu.guc.rolltheball.logic.grammer.Variable;

public class Unificiation {
	static ArrayList<Term[]> theta = new ArrayList<Term[]>();
	boolean done = false;
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

	public static boolean unify(Formula p, Formula q) {

		if ((q instanceof Predict) && (p instanceof Predict)) {
			if (!((Predict) q).name.equals(((Predict) p).name)) {
				System.out.println(p.toString());
				System.out.println(q.toString());
				System.out.println("-------------------------------------");
				theta.clear();
				return false;
			}

			if (q.toString().equals(p.toString())) {
				System.out.println(p.toString());
				System.out.println(q.toString());
				System.out.println("-------------------------------------");
				return true;
			}

			if (((Predict) q).terms.size() != ((Predict) p).terms.size()) {
				System.out.println(p.toString());
				System.out.println(q.toString());
				System.out.println("-------------------------------------");
				theta.clear();
				return false;
			}
			Term r = null;
			Term s = null;

			for (int i = 0; i < ((Predict) p).terms.size(); i++) {
				r = ((Predict) p).terms.get(i);
				s = ((Predict) q).terms.get(i);
				if (!r.toString().equals(s)) {
					System.out.println(p.toString());
					System.out.println(q.toString());
					System.out.println("-------------------------------------");
					if (r instanceof Variable) {
						 if (s instanceof Function)
						 if(((Function) s).terms.get(0) instanceof Variable)
						 if (((Variable) r).name.equals(((Variable)
						 ((Function) s).terms.get(0)).name))
						 break;

						Term[] terms = { r, s };
						theta.add(terms);
						return unify(subst(theta, p), subst(theta, q));
					}

					if (s instanceof Variable) {
						 if (r instanceof Function)
						 if(((Function) r).terms.get(0) instanceof Variable)
						 if (((Variable) s).name.equals(((Variable)
						 ((Function) r).terms.get(0)).name))
						 break;

						Term[] terms = { s, r };
						theta.add(terms);
						return unify(subst(theta, p), subst(theta, q));
					}

					if ((r instanceof Function) && (s instanceof Function)
							&& !((Function) r).toString().equals(((Function) s).toString())) {
						substFunc(r, s);
						return unify(subst(theta, p), subst(theta, q));

					}
				}
			}
			System.out.println(p.toString());
			System.out.println(q.toString());
			System.out.println("-------------------------------------");
			theta.clear();
			return false;
		}
		return false;
	}

	private static boolean substFunc(Term r, Term s) {
		if ((r instanceof Variable) && (s instanceof Variable)) {
			if (((Variable) r).name.equals(((Variable) s).name))
				return false;
			theta.add(new Term[] { s, r });
			return true;
		}

		if ((r instanceof Variable) && (s instanceof Constant)) {
			theta.add(new Term[] { r, s });
			return true;
		}

		if ((r instanceof Constant) && (s instanceof Variable)) {
			theta.add(new Term[] { s, r });
			return true;
		}

		if ((r instanceof Function) && (s instanceof Function)) {
			if (!((Function) r).name.equals(((Function) s).name))
				return true;
			substFunc(((Function) r).terms.get(0), ((Function) s).terms.get(0));
		}
		return false;
	}

	private static Formula subst(ArrayList<Term[]> theta, Formula x) {
		if (x instanceof Predict) {
			for (Term[] terms : theta) {
				for (Term term : ((Predict) x).terms) {
					if (term instanceof Variable) {
						if (term.toString().equals(terms[0].toString())) {
							((Predict) x).terms.set(((Predict) x).terms.indexOf(term), terms[1]);
						}
					} else {
						if (term instanceof Function) {
							for (Term fTerm : ((Function) term).terms) {
								if (fTerm instanceof Variable) {
									if (fTerm.toString().equals(terms[0].toString())) {
										((Function) term).terms.set(((Function) term).terms.indexOf(fTerm), terms[1]);
									}
								} else {
									if (fTerm instanceof Function) {
										for (Term f2Term : ((Function) fTerm).terms) {
											if (f2Term instanceof Variable) {
												if (f2Term.toString().equals(terms[0].toString())) {
													((Function) fTerm).terms
															.set(((Function) fTerm).terms.indexOf(f2Term), terms[1]);
												}
											}
										}

									}
								}
							}
						}
					}
				}
			}
		}
		return x;
	}

	public static void main(String[] args) {
		// parents(x, father(x), mother(Bill)) & parents(Bill, father(Bill), y)
		 Variable x = new Variable("x");
		 Function father1 = new Function("father", x);
		 Constant bill = new Constant("Bill");
		 Function mother1 = new Function("mother", bill);
		
		 Predict p = new Predict("parents", new Term[] {x,father1,mother1});
		
		 Variable y = new Variable("y");
		 Function father = new Function("father", bill);
		 Predict q = new Predict("parents", new Term[] {bill, father, y});

		// P(x; g(x); g(f(a))) and P(f(u); v; v)

//		Variable x = new Variable("x");
//		Function gOfX = new Function("g", x);
//		Constant a = new Constant("a");
//		Function fOfA = new Function("f", a);
//		Function gOfF = new Function("g", fOfA);
//		Predict p = new Predict("p", new Term[] { x, gOfX, gOfF });
//
//		Variable u = new Variable("u");
//		Variable v = new Variable("v");
//		Function fOfU = new Function("f", u);
//		Predict q = new Predict("p", new Term[] { fOfU, v, v });

		// P(a; y; f(y)) and P(z; z; u)

		// Constant a = new Constant("a");
		// Variable y = new Variable("y");
		// Function f = new Function("f", y);
		// Predict p = new Predict("p", new Term[] { a, y, f });
		//
		// Variable z = new Variable("z");
		// Variable u = new Variable("u");
		// Predict q = new Predict("p", new Term[] { z, z, u });

		// f(x; g(x); x) and f(g(u); g(g(z)); z)
//
//		 Variable x = new Variable("x");
//		 Function gOfX = new Function("g", x);
//		 Predict p = new Predict("f", new Term[] { x, gOfX, x });
//		
//		 Variable u = new Variable("u");
//		 Variable z = new Variable("z");
//		 Function gOfZ = new Function("g", z);
//		 Function gOfG = new Function("g", gOfZ);
//		 Function gOfU = new Function("g", u);
//		 Predict q = new Predict("f", new Term[] { gOfU, gOfG, z });
		System.out.println(unify(p, q));
		for (Term[] term : theta) {
			System.out.println("{" + term[0].toString() + "/" + term[1].toString() + "}");
		}
	}

}
