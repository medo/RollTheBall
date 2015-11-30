package eg.edu.guc.rolltheball.logic.grammer;

import java.util.ArrayList;

public class Predict implements Formula {
    String name;
    ArrayList<Term> terms;

    public Predict(String name, ArrayList<Term> terms) {
        super();
        this.name = name;
        this.terms = terms;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for(Term t : terms){
            buf.append(t.toString() + ",");
        }
        String s = buf.toString();
        if(s.charAt(s.length()-1) == ',')
            s = s.substring(0, s.length()-1);
        return name + "(" + s + ")";
    }
}
