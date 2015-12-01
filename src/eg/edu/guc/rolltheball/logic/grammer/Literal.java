package eg.edu.guc.rolltheball.logic.grammer;

public class Literal {
    public Predict predicate;
    public boolean negated;


    @Override
    public String toString(){
        return (negated? "¬" : "") + predicate.toString();
    }
}
