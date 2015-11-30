package eg.edu.guc.rolltheball.logic.grammer;

import java.util.ArrayList;

public class Predict implements Formula {
    public String name;
    public ArrayList<Term> terms;

    public Predict(String name, Term... terms) {
        super();
        this.name = name;
        this.terms = new ArrayList<Term>();
        for(Term t : terms){
            this.terms.add(t);
        }
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
