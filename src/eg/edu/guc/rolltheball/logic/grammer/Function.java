package eg.edu.guc.rolltheball.logic.grammer;

import java.util.ArrayList;

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
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for(Term t : terms){
            buf.append(t.toString() + ",");
        }
        String s = buf.toString();
        if(s.length() > 0 && s.charAt(s.length()-1) == ',')
            s = s.substring(0, s.length()-1);
        if(terms.size() > 0)
            return name + "(" + s + ")";
        else
            return name;
    }


}
