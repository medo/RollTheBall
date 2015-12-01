package eg.edu.guc.rolltheball.logic.grammer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Literal {
	
    public Predicate predicate;
    public boolean negated;

    @Override
    public boolean equals(Object obj) {
    	HashMap<Variable, Variable> sub = new HashMap<Variable, Variable>();
    	Literal lit = (Literal) obj;
    	if (lit.predicate.name.equals(predicate.name)) {
    		for (Term t : lit.predicate.terms) {
    			boolean found = true;
    			for (Term t1 : predicate.terms) {
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
    public String toString(){
        return (negated? "¬" : "") + predicate.toString();
    }
    
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
}
