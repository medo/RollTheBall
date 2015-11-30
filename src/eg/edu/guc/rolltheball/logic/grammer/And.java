package eg.edu.guc.rolltheball.logic.grammer;

public class And extends BinaryOperator implements Formula {

    public And(Formula left, Formula right) {
        super();
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + "∧" + right + ")";
    }
}
