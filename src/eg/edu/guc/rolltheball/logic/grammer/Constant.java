package eg.edu.guc.rolltheball.logic.grammer;

public class Constant extends Term {
    String name;

    public Constant(String name) {
        super();
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
