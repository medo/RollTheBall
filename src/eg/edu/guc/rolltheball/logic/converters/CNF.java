package eg.edu.guc.rolltheball.logic.converters;

import java.util.ArrayList;
import java.util.HashMap;

import eg.edu.guc.rolltheball.logic.grammer.And;
import eg.edu.guc.rolltheball.logic.grammer.BinaryOperator;
import eg.edu.guc.rolltheball.logic.grammer.Clause;
import eg.edu.guc.rolltheball.logic.grammer.Cloner;
import eg.edu.guc.rolltheball.logic.grammer.Equivalent;
import eg.edu.guc.rolltheball.logic.grammer.ForAll;
import eg.edu.guc.rolltheball.logic.grammer.Formula;
import eg.edu.guc.rolltheball.logic.grammer.Function;
import eg.edu.guc.rolltheball.logic.grammer.Implies;
import eg.edu.guc.rolltheball.logic.grammer.Literal;
import eg.edu.guc.rolltheball.logic.grammer.Not;
import eg.edu.guc.rolltheball.logic.grammer.Or;
import eg.edu.guc.rolltheball.logic.grammer.Predict;
import eg.edu.guc.rolltheball.logic.grammer.Quantifier;
import eg.edu.guc.rolltheball.logic.grammer.Term;
import eg.edu.guc.rolltheball.logic.grammer.ThereExist;
import eg.edu.guc.rolltheball.logic.grammer.Variable;

public class CNF {
    private static final String NEW_VAR_PREFIX = "v";
    private static final String NEW_FUN_PREFIX = "f";
    int newVarIdx = 1;
    int newFunIdx = 1;

    private Formula removeEquivilance(Formula f){
        if(f instanceof Equivalent){
            Equivalent tmp = (Equivalent) f;
            // Remove it to double implications
            f = new And(new Implies(Cloner.clone(tmp.left), Cloner.clone(tmp.right)), new Implies(Cloner.clone(tmp.right), Cloner.clone(tmp.left)));
            f = removeEquivilance(f);
        }else if(f instanceof BinaryOperator){
            ((BinaryOperator) f).left = removeEquivilance(((BinaryOperator) f).left);
            ((BinaryOperator) f).right = removeEquivilance(((BinaryOperator) f).right);
        }else if(f instanceof Not){
            ((Not) f).formula = removeEquivilance(((Not) f).formula);
        }else if(f instanceof Quantifier ){
            ((Quantifier) f).formula = removeEquivilance(((Quantifier) f).formula);
        }else if(f instanceof Predict){
            // Do Nothing
        }
        return f;
    }

    private Formula removeImplications(Formula f){
        if(f instanceof Implies){
            Implies tmp = (Implies) f;
            // Remove it to double implications
            f = new Or(new Not(tmp.left), tmp.right);
            f = removeImplications(f);
        }else if(f instanceof BinaryOperator){
            ((BinaryOperator) f).left = removeImplications(((BinaryOperator) f).left);
            ((BinaryOperator) f).right = removeImplications(((BinaryOperator) f).right);
        }else if(f instanceof Not){
            ((Not) f).formula = removeImplications(((Not) f).formula);
        }else if(f instanceof Quantifier ){
            ((Quantifier) f).formula = removeImplications(((Quantifier) f).formula);
        }else if(f instanceof Predict){
            // Do Nothing
        }
        return f;
    }


    private Formula pushNots(Formula f){
        if(f instanceof Not){
            Not tmp = (Not) f;
            if(tmp.formula instanceof Not){
                f = ((Not)tmp.formula).formula;
                f = pushNots(f);
            }else if(tmp.formula instanceof And){
                And t = (And) tmp.formula;
                f = new Or( new Not(t.left), new Not(t.right) );
                f = pushNots(f);
            }else if(tmp.formula instanceof Or){
                Or t = (Or) tmp.formula;
                f = new And( new Not(t.left), new Not(t.right) );
                f = pushNots(f);
            }else if(tmp.formula instanceof ForAll){
                ForAll t = (ForAll) tmp.formula;
                f = new ThereExist( t.var,  new Not(t.formula) );
                f = pushNots(f);
            }else if(tmp.formula instanceof ThereExist){
                ThereExist t = (ThereExist) tmp.formula;
                f = new ForAll( t.var,  new Not(t.formula) );
                f = pushNots(f);
            }

        }else if(f instanceof BinaryOperator){
            ((BinaryOperator) f).left = pushNots(((BinaryOperator) f).left);
            ((BinaryOperator) f).right = pushNots(((BinaryOperator) f).right);
        }else if(f instanceof Quantifier ){
            ((Quantifier) f).formula = pushNots(((Quantifier) f).formula);
        }else if(f instanceof Predict){
            // Do Nothing
        }
        return f;
    }

    private Term changeTermVariable(Term term, HashMap<String, String> mapping){
        if(term instanceof Function){
            ArrayList<Term> ts = new ArrayList<Term>();
            for(Term t: ((Function) term).terms){
                ts.add(changeTermVariable(t, mapping));
            }
            ((Function) term).terms = ts;
        }else if(term instanceof Variable){
            if(mapping.containsKey(((Variable) term).name)){
                ((Variable) term).name = mapping.get(((Variable) term).name);
            }
        }
        return term;
    }

    private Formula standardizeApart(Formula f, HashMap<String,String> mapping){
        if(f instanceof Not){
            ((Not) f).formula = standardizeApart(((Not) f).formula, mapping);
        }else if(f instanceof BinaryOperator){
            ((BinaryOperator) f).left = standardizeApart(((BinaryOperator) f).left, mapping);
            ((BinaryOperator) f).right = standardizeApart(((BinaryOperator) f).right, mapping);
        }else if(f instanceof Quantifier ){
            String newVar = NEW_VAR_PREFIX + (newVarIdx++);
            String oldVar = ((Quantifier) f).var.name;

            mapping.put(oldVar, newVar);
            ((Quantifier) f).formula = standardizeApart(((Quantifier) f).formula, mapping);
            ((Quantifier) f).var.name = newVar;
            mapping.remove(oldVar);

        }else if(f instanceof Predict){
            ArrayList<Term> ts = new ArrayList<Term>();
            for(Term t: ((Predict) f).terms){
                ts.add(changeTermVariable(t, mapping));
            }
            ((Predict) f).terms = ts;
        }
        return f;
    }

    private Term skolemizeTerm(Term term, HashMap<String, Function> sub){
        if(term instanceof Function){
            ArrayList<Term> ts = new ArrayList<Term>();
            for(Term t: ((Function) term).terms){
                ts.add(skolemizeTerm(t, sub));
            }
            ((Function) term).terms = ts;
        }else if(term instanceof Variable){
            if(sub.containsKey(((Variable) term).name)){
                term = sub.get(((Variable) term).name).clone();
            }
        }
        return term;
    }

    private Formula skolemize(Formula f, ArrayList<Variable> boundVars, HashMap<String, Function> sub){
        if(f instanceof Not){
            ((Not) f).formula = skolemize(((Not) f).formula, boundVars, sub);
        }else if(f instanceof BinaryOperator){
            ((BinaryOperator) f).left = skolemize(((BinaryOperator) f).left, boundVars, sub);
            ((BinaryOperator) f).right = skolemize(((BinaryOperator) f).right, boundVars, sub);
        }else if(f instanceof ForAll ){
            boundVars.add(((ForAll) f).var);
            ((ForAll) f).formula = skolemize(((ForAll) f).formula, boundVars, sub);
            boundVars.remove(boundVars.size()-1);
        }else if(f instanceof ThereExist ){
            String newFunName = NEW_FUN_PREFIX + (newFunIdx++);
            Function newFun = new Function(newFunName, new ArrayList<Variable>(boundVars).toArray(new Term[boundVars.size()]));
            String oldName = ((ThereExist) f).var.name;
            sub.put(oldName, newFun);
            f = skolemize(((ThereExist) f).formula, boundVars, sub);
            sub.remove(oldName);
        }else if(f instanceof Predict){
            ArrayList<Term> ts = new ArrayList<Term>();
            for(Term t: ((Predict) f).terms){
                ts.add(skolemizeTerm(t, sub));
            }
            ((Predict) f).terms = ts;
        }
        return f;
    }


    private Formula discardUniversalQuantifier(Formula f){
        if(f instanceof Not){
            ((Not) f).formula = discardUniversalQuantifier(((Not) f).formula);
        }else if(f instanceof BinaryOperator){
            ((BinaryOperator) f).left = discardUniversalQuantifier(((BinaryOperator) f).left);
            ((BinaryOperator) f).right = discardUniversalQuantifier(((BinaryOperator) f).right);
        }else if(f instanceof ForAll ){
            f = discardUniversalQuantifier(((ForAll) f).formula);
        }else if(f instanceof Predict){
            // Do Nothing
        }
        return f;
    }


    private Formula distributeAndsOrs(Formula f){
        if(f instanceof Not){
            ((Not) f).formula = distributeAndsOrs(((Not) f).formula);
        }else if(f instanceof And){
            ((And) f).left = distributeAndsOrs(((And) f).left);
            ((And) f).right = distributeAndsOrs(((And) f).right);
        }else if(f instanceof Or){
            if( ((Or) f).right instanceof And){
                Formula other = (((Or) f).left);
                And and = (And) (((Or) f).right);
                f = new And( new Or(Cloner.clone(other), and.left), new Or(Cloner.clone(other), and.right));
                f = distributeAndsOrs(f);
            }else if(((Or) f).left instanceof And){
                And and = (And) (((Or) f).left);
                Formula other = (((Or) f).right);
                f = new And( new Or(Cloner.clone(other), and.left), new Or(Cloner.clone(other), and.right));
                f = distributeAndsOrs(f);
            }else{
                ((Or) f).left = distributeAndsOrs(((Or) f).left);
                ((Or) f).right = distributeAndsOrs(((Or) f).right);
            }
        }else if(f instanceof Predict){
            // Do Nothing
        }
        return f;
    }

    private Clause getClause(Formula f){
        Clause ret = new Clause();
        if(f instanceof Not){
            Literal l = new Literal();
            l.negated = true;
            l.predicate = (Predict) ((Not) f).formula;
            ret.add(l);
        }else if(f instanceof Predict){
            Literal l = new Literal();
            l.negated = false;
            l.predicate = (Predict) f;
            ret.add(l);
        }else if(f instanceof Or){
            ret.addAll(getClause(((Or) f).left));
            ret.addAll(getClause(((Or) f).right));
        }

        return ret;
    }

    private ArrayList<Clause> clausify(Formula f) {
        ArrayList<Clause> ret = new ArrayList<Clause>();

        if(f instanceof Not || f instanceof Predict || f instanceof Or){
            ret.add(getClause(f));
        }else if(f instanceof And){
            ret.addAll(clausify(((And) f).left));
            ret.addAll(clausify(((And) f).right));
        }
        return ret;
    }

    private Term renameVarInTerm(Term f, HashMap<String,String> sub){
        if(f instanceof Variable){
            if(sub.containsKey(((Variable) f).name)){
                ((Variable) f).name = sub.get(((Variable) f).name);
            }else{
                String newName = NEW_VAR_PREFIX + (newVarIdx++);
                sub.put(((Variable) f).name, newName);
                ((Variable) f).name = newName;
            }
        }else if(f instanceof Function){
            ArrayList<Term> ts = new ArrayList<Term>();
            for(Term t : ((Function) f).terms){
                ts.add(renameVarInTerm(t, sub));
            }
            ((Function) f).terms = ts;
        }
        return f;
    }

    private ArrayList<Clause> renameVarsInClause(ArrayList<Clause> cl){

        for(Clause c : cl){
            HashMap<String,String> sub = new HashMap<String, String>();
            for(Literal l : c){
                ArrayList<Term> ts = new ArrayList<Term>();
                for(Term t : l.predicate.terms){
                    ts.add(renameVarInTerm(t, sub));
                }
                l.predicate.terms = ts;
            }
        }
        return cl;
    }

    public ArrayList<Clause> convertToCNF(Formula f){
        System.out.println(f);
        // Remove Equivilance
        f = removeEquivilance(f);
        System.out.println(f);

        // Remove Implications
        f = removeImplications(f);
        System.out.println(f);

        // Push Nots
        f = pushNots(f);
        System.out.println(f);

        // Standardize Apart
        f = standardizeApart(f, new HashMap<String,String>());
        System.out.println(f);


        // Skolemize
        f = skolemize(f, new ArrayList<Variable>(), new HashMap<String,Function>());
        System.out.println(f);

        // Discard Universal Quantifiers
        f = discardUniversalQuantifier(f);
        System.out.println(f);

        // Distribute Ands and Ors
        f = distributeAndsOrs(f);
        System.out.println(f);


        ArrayList<Clause> cl = clausify(f);
        System.out.println(cl);

        return renameVarsInClause(cl);
    }


    public static void main(String[] args){

        Predict p1 = new Predict("Q", new Variable("x"));

        Predict p2 = new Predict("Z", new Variable("y"));

        Formula p = new And(new Not(p1), p2);

        Predict p3 = new Predict("P", new Variable("z"));

        Formula x = new Equivalent(p, p3);


        Formula y = new Not(new ForAll(new Variable("x"), p3 ));


        Formula z = new And(new ForAll(new Variable("x"), p3 ), new ForAll(new Variable("x"), p1 ));

        Formula zz = new And(
                new ForAll(new Variable("x"), new ThereExist(
                        new Variable("y"), new Predict("Q", new Variable("y"))
                        )
                ),
                new ThereExist(new Variable("z"), new Predict("P", new Variable("z")))
                );

        Formula xz = new Or(
                    new And(new Predict("P", new Variable("x")), new Predict("Q", new Variable("x"))),
                    new And(new Predict("S", new Variable("x")), new Predict("R", new Variable("x")))
                );

        Formula test = new Or(
                new And(new Predict("a"), new Predict("b")),
                new And(
                        new Not(new Predict("c")),
                        new Or(new Predict("d"), new Predict("e"))
                    )
                );


        Formula Example1 = new ThereExist(new Variable("x"), new And(
                    new Predict("P", new Variable("x")),
                    new ForAll(new Variable("x"), new Implies(
                            new Predict("Q", new Variable("x")),
                            new Not(new Predict("P", new Variable("x")))
                     ))
                ));

        Formula Example2 = new ForAll(new Variable("x"), new Equivalent(
                    new Predict("P", new Variable("x")),
                    new And(
                        new Predict("Q", new Variable("x")),
                        new ThereExist(new Variable("y"), new And(
                            new Predict("Q", new Variable("y")),
                            new Predict("R", new Variable("y"), new Variable("x"))
                           )
                        )
                     )

                ));
        System.out.println(new CNF().convertToCNF(Example2));

    }

}
