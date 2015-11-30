package eg.edu.guc.rolltheball.logic.grammer;

public class Or extends BinaryOperator implements Formula {


    public Or(Formula left, Formula right) {
        super();
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + "∨" + right + ")";
    }

}
