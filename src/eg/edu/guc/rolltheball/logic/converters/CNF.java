package eg.edu.guc.rolltheball.logic.converters;

import java.util.ArrayList;
import java.util.HashMap;

import eg.edu.guc.rolltheball.logic.grammer.And;
import eg.edu.guc.rolltheball.logic.grammer.BinaryOperator;
import eg.edu.guc.rolltheball.logic.grammer.Equivalent;
import eg.edu.guc.rolltheball.logic.grammer.ForAll;
import eg.edu.guc.rolltheball.logic.grammer.Formula;
import eg.edu.guc.rolltheball.logic.grammer.Function;
import eg.edu.guc.rolltheball.logic.grammer.Implies;
import eg.edu.guc.rolltheball.logic.grammer.Not;
import eg.edu.guc.rolltheball.logic.grammer.Or;
import eg.edu.guc.rolltheball.logic.grammer.Predicate;
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
            f = new And(new Implies(tmp.left, tmp.right), new Implies(tmp.right, tmp.left));
            f = removeEquivilance(f);
        }else if(f instanceof BinaryOperator){
            ((BinaryOperator) f).left = removeEquivilance(((BinaryOperator) f).left);
            ((BinaryOperator) f).right = removeEquivilance(((BinaryOperator) f).right);
        }else if(f instanceof Not){
            ((Not) f).formula = removeEquivilance(((Not) f).formula);
        }else if(f instanceof Quantifier ){
            ((Quantifier) f).formula = removeEquivilance(((Quantifier) f).formula);
        }else if(f instanceof Predicate){
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
        }else if(f instanceof Predicate){
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
            ((Quantifier) f).formula = removeImplications(((Quantifier) f).formula);
        }else if(f instanceof Predicate){
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

        }else if(f instanceof Predicate){
            ArrayList<Term> ts = new ArrayList<Term>();
            for(Term t: ((Predicate) f).terms){
                ts.add(changeTermVariable(t, mapping));
            }
            ((Predicate) f).terms = ts;
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
        }else if(f instanceof Predicate){
            ArrayList<Term> ts = new ArrayList<Term>();
            for(Term t: ((Predicate) f).terms){
                ts.add(skolemizeTerm(t, sub));
            }
            ((Predicate) f).terms = ts;
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
        }else if(f instanceof Predicate){
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
                f = new And( new Or(other, and.left), new Or(other, and.right));
                f = distributeAndsOrs(f);
            }else if(((Or) f).left instanceof And){
                And and = (And) (((Or) f).left);
                Formula other = (((Or) f).right);
                f = new And( new Or(other, and.left), new Or(other, and.right));
                f = distributeAndsOrs(f);
            }else{
                ((Or) f).left = distributeAndsOrs(((Or) f).left);
                ((Or) f).right = distributeAndsOrs(((Or) f).right);
            }
        }else if(f instanceof Predicate){
            // Do Nothing
        }
        return f;
    }

    public Formula convertToCNF(Formula f){
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
        return f;
    }

    public static void main(String[] args){

        Predicate p1 = new Predicate("Q", new Variable("x"));

        Predicate p2 = new Predicate("Z", new Variable("y"));

        Formula p = new And(new Not(p1), p2);

        Predicate p3 = new Predicate("P", new Variable("z"));

        Formula x = new Equivalent(p, p3);


        Formula y = new Not(new ForAll(new Variable("x"), p3 ));


        Formula z = new And(new ForAll(new Variable("x"), p3 ), new ForAll(new Variable("x"), p1 ));

        Formula zz = new And(
                new ForAll(new Variable("x"), new ThereExist(
                        new Variable("y"), new Predicate("Q", new Variable("y"))
                        )
                ),
                new ThereExist(new Variable("z"), new Predicate("P", new Variable("z")))
                );

        Formula xz = new Or(
                    new And(new Predicate("P", new Variable("x")), new Predicate("Q", new Variable("x"))),
                    new And(new Predicate("S", new Variable("x")), new Predicate("R", new Variable("x")))
                );

        Formula test = new Or(
                new And(new Predicate("a"), new Predicate("b")),
                new And(
                        new Not(new Predicate("c")),
                        new Or(new Predicate("d"), new Predicate("e"))
                    )
                );


        new CNF().convertToCNF(test);

    }

}
