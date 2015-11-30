package eg.edu.guc.rolltheball.logic.grammer;

public class Implies extends BinaryOperator implements Formula {

    public Implies(Formula left, Formula right) {
        super();
        this.left = left;
        this.right = right;
    }


    @Override
    public String toString() {
        return "(" + left + "⇒" + right + ")";
    }

}
