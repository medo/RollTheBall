package eg.edu.guc.rolltheball.logic.grammer;

import java.util.ArrayList;
import java.util.HashMap;

public class Function extends Term{
    public String name;
    public ArrayList<Term> terms;

    public Function(String name, Term... terms) {
        super();
        this.name = name;
        this.terms = new ArrayList<Term>();
        for(Term t : terms){
            this.terms.add(t);
        }

    }

    @Override
    public Function clone(){
        ArrayList<Term> ts = new ArrayList<Term>();
        for(Term t : this.terms){
            if( t instanceof Function)
                ts.add(((Function) t).clone());
            else if( t instanceof Constant)
                ts.add(((Constant) t).clone());
            else if( t instanceof Variable)
                ts.add(((Variable) t).clone());
        }
        return new Function(this.name, ts.toArray(new Term[ts.size()]));


    }

    @Override
    public boolean equals(Object obj) {
    	HashMap<Variable, Variable> sub = new HashMap<Variable, Variable>();
    	Function fun = (Function) obj;
    	if (fun.name.equals(fun.name)) {
    		for (Term t : fun.terms) {
    			for (Term t1 : terms) {
    				if (t instanceof Variable && t1 instanceof Variable){
    					Variable v1 = (Variable) t;
    					Variable v2 = (Variable) t1;
    					if (sub.containsKey(v1) && sub.get(v1) != v2 || sub.containsKey(v2) && sub.get(v2) != v1) return false;
    					sub.put(v1, v2);
    					continue;
    				}
    				if (t instanceof Variable || t1 instanceof Variable) return false;
    				if (!t.equals(t1)) return false;
    			}
    		}
    		return true;
    	}
    	return false;
    }
    
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for(Term t : terms){
            buf.append(t.toString() + ",");
        }
        String s = buf.toString();
        if(s.length() > 0 && s.charAt(s.length()-1) == ',')
            s = s.substring(0, s.length()-1);
        return name + "(" + s + ")";
    }


}
