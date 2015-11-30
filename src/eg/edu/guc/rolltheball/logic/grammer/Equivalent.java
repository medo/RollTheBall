package eg.edu.guc.rolltheball.logic.grammer;

public class Equivalent extends BinaryOperator implements Formula {

    public Equivalent(Formula left, Formula right) {
        super();
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + "⇐⇒" + right + ")";
    }
}
