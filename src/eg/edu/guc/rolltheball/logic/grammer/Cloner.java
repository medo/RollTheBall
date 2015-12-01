package eg.edu.guc.rolltheball.logic.grammer;

import java.util.ArrayList;

public class Cloner {

    public static Formula clone(Formula f){
        Formula ret = null;

        if(f instanceof And){
            ret = new And(clone(((And) f).left), clone(((And) f).right));
        }else if(f instanceof Or){
            ret = new Or(clone(((Or) f).left), clone(((Or) f).right));
        }else if(f instanceof Implies){
            ret = new Implies(clone(((Implies) f).left), clone(((Implies) f).right));
        }else if(f instanceof Equivalent){
            ret = new Equivalent(clone(((Equivalent) f).left), clone(((Equivalent) f).right));
        }else if(f instanceof Not){
            ret = new Not(clone(((Not) f).formula));
        }else if(f instanceof Predict){
            ArrayList<Term> ts = new ArrayList<Term>();
            for(Term t : ((Predict)f).terms){
                if( t instanceof Function)
                    ts.add(((Function) t).clone());
                else if( t instanceof Constant)
                    ts.add(((Constant) t).clone());
                else if( t instanceof Variable)
                    ts.add(((Variable) t).clone());
            }
            ret = new Predict(((Predict) f).name, ts.toArray(new Term[ts.size()]));
        }else if(f instanceof ForAll){
            ret = new ForAll(((ForAll) f).var.clone(), clone(((ForAll) f).formula));
        }else if(f instanceof ThereExist){
            ret = new ThereExist(((ThereExist) f).var.clone(), clone(((ThereExist) f).formula));
        }
        return ret;
    }

}
