package eg.edu.guc.rolltheball.logic.grammer;

public class Variable extends Term {
    String name;


    public Variable(String name) {
        super();
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
